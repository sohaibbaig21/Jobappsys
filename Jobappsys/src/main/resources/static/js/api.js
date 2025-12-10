// Minimal API helper for the Jobappsys frontend
(() => {
  const STORAGE_KEY = 'auth';
  // FIX: Define where your Spring Boot backend lives
  const API_BASE = 'http://localhost:8081';

  function getAuth() {
    try { return JSON.parse(localStorage.getItem(STORAGE_KEY) || 'null'); } catch { return null; }
  }

  function setAuth(auth) {
    if (!auth) localStorage.removeItem(STORAGE_KEY);
    else localStorage.setItem(STORAGE_KEY, JSON.stringify(auth));
  }

  function clearAuth() { localStorage.removeItem(STORAGE_KEY); }

  async function apiFetch(path, options = {}) {
    // FIX: If path is relative (starts with /), prepend the API_BASE
    const url = path.startsWith('http') ? path : API_BASE + path;

    const headers = new Headers(options.headers || {});

    // Add JSON defaults if body is plain object
    if (options.body && typeof options.body === 'object' && !(options.body instanceof FormData)) {
      headers.set('Content-Type', 'application/json');
      options.body = JSON.stringify(options.body);
    }

    // Add auth token if present
    const auth = getAuth();
    if (auth?.token && options.auth !== false) {
      const tokenType = auth.tokenType || 'Bearer';
      headers.set('Authorization', `${tokenType} ${auth.token}`);
    }

    const resp = await fetch(url, { ...options, headers });

    // Handle response parsing
    const contentType = resp.headers.get('content-type') || '';
    const isJson = contentType.includes('application/json');
    const data = isJson ? await resp.json().catch(() => null) : await resp.text();

    if (!resp.ok) {
      const msg = isJson && data && (data.message || data.error) ? (data.message || data.error) : resp.statusText;
      const err = new Error(msg || `HTTP ${resp.status}`);
      err.status = resp.status;
      err.data = data;
      throw err;
    }
    return data;
  }

  function qs(name) {
    const params = new URLSearchParams(location.search);
    return params.get(name);
  }

  // DOM Helper
  function el(tag, props = {}, children = []) {
    const node = document.createElement(tag);
    Object.entries(props).forEach(([k, v]) => {
      if (k === 'class') node.className = v;
      else if (k === 'text') node.textContent = v;
      else if (k.startsWith('on') && typeof v === 'function') node.addEventListener(k.substring(2), v);
      else node.setAttribute(k, v);
    });
    if (!Array.isArray(children)) children = [children];
    children.filter(Boolean).forEach(c => node.append(c instanceof Node ? c : document.createTextNode(String(c))));
    return node;
  }

  // Navigation Header
// Updated renderHeader in api.js
  function renderHeader(active = '') {
    const auth = getAuth();

    // Define Links
    const links = [];
    links.push(el('a', { href: '/jobs.html', class: active === 'jobs' ? 'active' : '' }, 'Jobs'));

    if (auth) {
        if (auth.role === 'EMPLOYER') {
            links.push(el('a', { href: '/post-job.html', class: active === 'post-job' ? 'active' : '' }, 'Post Job'));
        }
        // Link to Profile Setup / View
        if (auth.role === 'APPLICANT') {
            links.push(el('a', { href: '/profile-setup.html', class: active === 'profile' ? 'active' : '' }, 'My Profile'));
        }
    }

    // Role Badge Logic
    let userDisplay = '';
    if (auth) {
        // Show "Applicant" or "Employer" next to email
        const roleBadge = auth.role ? ` (${auth.role})` : '';
        userDisplay = (auth.email || 'User') + roleBadge;
    }

    const nav = el('header', { class: 'navbar' }, [
      el('a', { href: '/index.html', class: 'brand' }, 'JobAppSys'),
      el('nav', { class: 'nav-links' }, links),
      el('div', { class: 'spacer' }),
      auth ? el('div', { class: 'user-menu' }, [
        el('span', { class: 'muted' }, userDisplay), // <--- Shows Role now
        el('button', { type: 'button', class: 'btn-logout', onclick: () => { clearAuth(); location.href = '/index.html'; } }, 'Logout')
      ]) : el('div', {}, [
          // FIX: Ensure clicking Login goes to login.html, not jobs
          el('a', { href: '/index.html' }, 'Login'),
          el('span', {text: ' | '}),
          el('a', { href: '/register.html' }, 'Register')
      ])
    ]);

    // Remove existing header if any (prevents duplicates on re-render)
    const existing = document.querySelector('header');
    if (existing) existing.remove();

    document.body.prepend(nav);
  }

    const nav = el('header', { class: 'navbar' }, [
      el('a', { href: '/index.html', class: 'brand' }, 'JobAppSys'),
      el('nav', { class: 'nav-links' }, links),
      el('div', { class: 'spacer' }),
      auth ? el('div', { class: 'user-menu' }, [
        el('span', { class: 'muted' }, auth.email || 'User'),
        el('button', { type: 'button', class: 'btn-logout', onclick: () => { clearAuth(); location.href = '/index.html'; } }, 'Logout')
      ]) : el('div', {}, [
          el('a', { href: '/index.html' }, 'Login'),
          el('span', {text: ' | '}),
          el('a', { href: '/register.html' }, 'Register')
      ])
    ]);
    document.body.prepend(nav);
  }

  window.$api = { apiFetch, getAuth, setAuth, clearAuth, qs, el, renderHeader };
})();