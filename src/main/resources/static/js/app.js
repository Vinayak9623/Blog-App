/**
 * Modern Blog App - Vanilla JavaScript Core Library
 * Handles Authentication, REST API Communication, UI Helpers, and Theme
 */

// ==========================================================================
// Authentication Manager
// ==========================================================================
const Auth = {
  TOKEN_KEY: 'blog_access_token',
  REFRESH_KEY: 'blog_refresh_token',
  USER_KEY: 'blog_user_data',

  getToken() {
    return localStorage.getItem(this.TOKEN_KEY);
  },

  getRefreshToken() {
    return localStorage.getItem(this.REFRESH_KEY);
  },

  // Helper to decode JWT token payload without external libraries
  decodeTokenPayload(token) {
    if (!token) return null;
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      return JSON.parse(jsonPayload);
    } catch (e) {
      console.warn('Could not decode JWT payload:', e);
      return null;
    }
  },

  getUser() {
    const data = localStorage.getItem(this.USER_KEY);
    let user = null;
    try {
      user = data ? JSON.parse(data) : {};
    } catch (e) {
      user = {};
    }

    // Ensure role is extracted from JWT token payload if missing
    const token = this.getToken();
    if (token) {
      const payload = this.decodeTokenPayload(token);
      if (payload) {
        if (!user.email && payload.sub) user.email = payload.sub;
        if (payload.role) user.role = payload.role;
      }
    }

    return user;
  },

  isLoggedIn() {
    return !!this.getToken();
  },

  isAdmin() {
    const token = this.getToken();
    if (!token) return false;

    // 1. Check decoded JWT claims directly from accessToken
    const payload = this.decodeTokenPayload(token);
    if (payload && (payload.role === 'ROLE_ADMIN' || payload.role === 'ADMIN')) {
      return true;
    }

    // 2. Check user object in localStorage
    const user = this.getUser();
    if (user && (user.role === 'ROLE_ADMIN' || user.role === 'ADMIN')) {
      return true;
    }

    return false;
  },

  saveAuth(authResponse) {
    if (authResponse.accessToken) {
      localStorage.setItem(this.TOKEN_KEY, authResponse.accessToken);
    }
    if (authResponse.refreshToken) {
      localStorage.setItem(this.REFRESH_KEY, authResponse.refreshToken);
    }

    let user = authResponse.user || {};
    
    // Automatically extract role and subject from the JWT accessToken
    if (authResponse.accessToken) {
      const payload = this.decodeTokenPayload(authResponse.accessToken);
      if (payload) {
        if (payload.role) user.role = payload.role;
        if (payload.sub && !user.email) user.email = payload.sub;
      }
    }

    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  },

  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_KEY);
    localStorage.removeItem(this.USER_KEY);
    window.location.href = '/login';
  },

  requireAuth(redirectUrl = window.location.pathname) {
    if (!this.isLoggedIn()) {
      UI.showToast('Please login to continue', 'warning');
      setTimeout(() => {
        window.location.href = `/login?redirect=${encodeURIComponent(redirectUrl)}`;
      }, 1000);
      return false;
    }
    return true;
  },

  requireAdmin() {
    if (!this.isLoggedIn()) {
      window.location.href = '/login?redirect=/admin';
      return false;
    }
    if (!this.isAdmin()) {
      UI.showToast('Access denied: Administrator privileges required', 'error');
      setTimeout(() => {
        window.location.href = '/';
      }, 1500);
      return false;
    }
    return true;
  },

  updateNavbar() {
    const navAuthContainer = document.getElementById('nav-auth-container');
    const writeStoryBtn = document.getElementById('nav-write-btn');
    const adminLink = document.getElementById('nav-admin-link');
    const dashboardLink = document.getElementById('nav-dashboard-link');

    if (!navAuthContainer) return;

    if (this.isLoggedIn()) {
      const user = this.getUser() || { name: 'User', email: '', role: 'ROLE_GUEST' };
      const initials = (user.name || user.email || 'U').substring(0, 2).toUpperCase();
      const isAdmin = this.isAdmin();

      if (writeStoryBtn) writeStoryBtn.style.display = 'inline-flex';
      if (dashboardLink) dashboardLink.style.display = 'inline-flex';
      if (adminLink) adminLink.style.display = isAdmin ? 'inline-flex' : 'none';

      navAuthContainer.innerHTML = `
        <div class="user-menu" id="userMenuWrapper">
          <button class="user-avatar-btn" id="userAvatarBtn" onclick="Auth.toggleUserDropdown()">
            <div class="avatar-circle">${initials}</div>
            <span class="user-name-display">${UI.escapeHtml(user.name || user.email || 'Account')}</span>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="6 9 12 15 18 9"></polyline></svg>
          </button>
          <div class="dropdown-menu" id="userDropdownMenu">
            <div class="dropdown-header">
              <div class="dropdown-user-name">${UI.escapeHtml(user.name || 'Author')}</div>
              <div class="dropdown-user-email">${UI.escapeHtml(user.email || '')}</div>
              <span class="dropdown-user-role">${isAdmin ? 'Administrator' : 'Author'}</span>
            </div>
            <a href="/dashboard" class="dropdown-item">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path><polyline points="22,6 12,13 2,6"></polyline></svg>
              My Stories & Dashboard
            </a>
            <a href="/create-article" class="dropdown-item">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
              Write New Story
            </a>
            ${isAdmin ? `
            <a href="/admin" class="dropdown-item">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path></svg>
              Admin Control Panel
            </a>` : ''}
            <button onclick="Auth.logout()" class="dropdown-item danger" style="width: 100%; border: none; background: none; text-align: left;">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" y1="12" x2="9" y2="12"></line></svg>
              Sign Out
            </button>
          </div>
        </div>
      `;
    } else {
      if (writeStoryBtn) writeStoryBtn.style.display = 'none';
      if (dashboardLink) dashboardLink.style.display = 'none';
      if (adminLink) adminLink.style.display = 'none';

      navAuthContainer.innerHTML = `
        <a href="/login" class="btn btn-outline btn-sm">Sign In</a>
        <a href="/register" class="btn btn-primary btn-sm">Get Started</a>
      `;
    }
  },

  toggleUserDropdown() {
    const menu = document.getElementById('userDropdownMenu');
    if (menu) {
      menu.classList.toggle('show');
    }
  }
};

// Close dropdown when clicking outside
document.addEventListener('click', (e) => {
  const wrapper = document.getElementById('userMenuWrapper');
  const menu = document.getElementById('userDropdownMenu');
  if (wrapper && menu && !wrapper.contains(e.target)) {
    menu.classList.remove('show');
  }
});

// ==========================================================================
// REST API Client
// ==========================================================================
const API = {
  BASE_URL: '',

  async request(endpoint, options = {}) {
    const token = Auth.getToken();
    const headers = {
      ...options.headers
    };

    if (!(options.body instanceof FormData) && !headers['Content-Type']) {
      headers['Content-Type'] = 'application/json';
    }

    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const config = {
      ...options,
      headers
    };

    try {
      const response = await fetch(`${this.BASE_URL}${endpoint}`, config);

      if (response.status === 401) {
        if (Auth.isLoggedIn()) {
          UI.showToast('Session expired. Please sign in again.', 'warning');
          Auth.logout();
        }
        throw new Error('Unauthorized');
      }

      if (response.status === 403) {
        UI.showToast('You do not have permission to perform this action.', 'error');
        throw new Error('Forbidden');
      }

      if (!response.ok) {
        let errorMsg = `Error ${response.status}: ${response.statusText}`;
        try {
          const errData = await response.json();
          if (errData.message) errorMsg = errData.message;
        } catch (e) {
          // not json
        }
        throw new Error(errorMsg);
      }

      const text = await response.text();
      return text ? JSON.parse(text) : null;
    } catch (err) {
      console.error(`API Error on [${options.method || 'GET'}] ${endpoint}:`, err);
      throw err;
    }
  },

  get(endpoint) {
    return this.request(endpoint, { method: 'GET' });
  },

  post(endpoint, body) {
    return this.request(endpoint, {
      method: 'POST',
      body: JSON.stringify(body)
    });
  },

  put(endpoint, body) {
    return this.request(endpoint, {
      method: 'PUT',
      body: JSON.stringify(body)
    });
  },

  delete(endpoint) {
    return this.request(endpoint, { method: 'DELETE' });
  },

  upload(endpoint, formData) {
    return this.request(endpoint, {
      method: 'POST',
      body: formData
    });
  }
};

// ==========================================================================
// UI Helpers & Utilities
// ==========================================================================
const UI = {
  showToast(message, type = 'info', duration = 3500) {
    let container = document.getElementById('toastContainer');
    if (!container) {
      container = document.createElement('div');
      container.id = 'toastContainer';
      container.className = 'toast-container';
      document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;

    let iconSvg = '';
    if (type === 'success') {
      iconSvg = '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#10b981" stroke-width="2"><polyline points="20 6 9 17 4 12"></polyline></svg>';
    } else if (type === 'error') {
      iconSvg = '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#ef4444" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>';
    } else {
      iconSvg = '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#4f46e5" stroke-width="2"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="16" x2="12" y2="12"></line><line x1="12" y1="8" x2="12.01" y2="8"></line></svg>';
    }

    toast.innerHTML = `
      ${iconSvg}
      <div style="flex: 1;">${this.escapeHtml(message)}</div>
      <button onclick="this.parentElement.remove()" style="background:none; border:none; color:var(--text-muted); cursor:pointer; font-size:1.1rem;">&times;</button>
    `;

    container.appendChild(toast);

    setTimeout(() => {
      if (toast.parentElement) {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(20px)';
        toast.style.transition = 'all 0.3s ease';
        setTimeout(() => toast.remove(), 300);
      }
    }, duration);
  },

  escapeHtml(str) {
    if (!str) return '';
    return String(str)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#039;');
  },

  formatDate(dateStr) {
    if (!dateStr) return 'Recently';
    try {
      const d = new Date(dateStr);
      if (isNaN(d.getTime())) return 'Recently';
      return d.toLocaleDateString('en-US', {
        month: 'short',
        day: 'numeric',
        year: 'numeric'
      });
    } catch (e) {
      return 'Recently';
    }
  },

  calculateReadTime(text) {
    if (!text) return '1 min read';
    const words = text.trim().split(/\s+/).length;
    const minutes = Math.max(1, Math.ceil(words / 200));
    return `${minutes} min read`;
  },

  getDefaultCover(categoryName = 'Technology', title = 'Article') {
    const gradients = [
      'linear-gradient(135deg, #4f46e5, #06b6d4)',
      'linear-gradient(135deg, #10b981, #3b82f6)',
      'linear-gradient(135deg, #f59e0b, #ec4899)',
      'linear-gradient(135deg, #8b5cf6, #3b82f6)',
      'linear-gradient(135deg, #0ea5e9, #6366f1)'
    ];
    const index = Math.abs((categoryName + title).split('').reduce((acc, char) => acc + char.charCodeAt(0), 0)) % gradients.length;
    const bg = gradients[index];

    return `
      <div class="card-fallback-cover" style="background: ${bg};">
        <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
          <path d="M19 20H5a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2v1m2 13a2 2 0 0 1-2-2V7m2 13a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z"></path>
        </svg>
        <span style="font-weight: 700; font-size: 0.95rem; opacity: 0.9;">${UI.escapeHtml(categoryName)}</span>
      </div>
    `;
  },

  renderRatingStars(rating = 5.0) {
    const safeRating = Math.min(5, Math.max(0, rating || 5));
    return `
      <div style="display: inline-flex; align-items: center; gap: 2px;">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="#f59e0b" stroke="#f59e0b"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon></svg>
        <span style="font-size: 0.8rem; font-weight: 700; color: var(--text-main); margin-left: 2px;">${safeRating.toFixed(1)}</span>
      </div>
    `;
  },

  confirm(title, message, onConfirm) {
    let modal = document.getElementById('confirmModal');
    if (!modal) {
      modal = document.createElement('div');
      modal.id = 'confirmModal';
      modal.className = 'modal-backdrop';
      modal.innerHTML = `
        <div class="modal-box">
          <div class="modal-header">
            <h3 class="modal-title" id="confirmModalTitle">Confirm Action</h3>
            <button class="modal-close-btn" onclick="document.getElementById('confirmModal').classList.remove('show')">&times;</button>
          </div>
          <div class="modal-body" id="confirmModalMessage">
            Are you sure you want to proceed?
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary btn-sm" onclick="document.getElementById('confirmModal').classList.remove('show')">Cancel</button>
            <button class="btn btn-danger btn-sm" id="confirmModalOkBtn">Confirm</button>
          </div>
        </div>
      `;
      document.body.appendChild(modal);
    }

    document.getElementById('confirmModalTitle').textContent = title;
    document.getElementById('confirmModalMessage').textContent = message;
    const okBtn = document.getElementById('confirmModalOkBtn');

    const newOkBtn = okBtn.cloneNode(true);
    okBtn.parentNode.replaceChild(newOkBtn, okBtn);

    newOkBtn.addEventListener('click', () => {
      modal.classList.remove('show');
      if (typeof onConfirm === 'function') onConfirm();
    });

    modal.classList.add('show');
  },

  initTheme() {
    const savedTheme = localStorage.getItem('blog_theme') || 'light';
    document.documentElement.setAttribute('data-theme', savedTheme);
    this.updateThemeIcon(savedTheme);
  },

  toggleTheme() {
    const current = document.documentElement.getAttribute('data-theme') || 'light';
    const next = current === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', next);
    localStorage.setItem('blog_theme', next);
    this.updateThemeIcon(next);
  },

  updateThemeIcon(theme) {
    const btn = document.getElementById('themeToggleBtn');
    if (!btn) return;
    if (theme === 'dark') {
      btn.innerHTML = `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="5"></circle><line x1="12" y1="1" x2="12" y2="3"></line><line x1="12" y1="21" x2="12" y2="23"></line><line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line><line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line><line x1="1" y1="12" x2="3" y2="12"></line><line x1="21" y1="12" x2="23" y2="12"></line><line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line><line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line></svg>`;
    } else {
      btn.innerHTML = `<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path></svg>`;
    }
  }
};

// ==========================================================================
// Initialize on DOM Ready
// ==========================================================================
document.addEventListener('DOMContentLoaded', () => {
  UI.initTheme();
  Auth.updateNavbar();
});
