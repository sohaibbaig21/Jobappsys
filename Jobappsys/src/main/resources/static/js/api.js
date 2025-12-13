const $api = {

    // 0. BASE URL: route API calls to backend when UI runs on 8081
    _apiBase: (() => {
        try {
            const { protocol, hostname, port } = window.location;
            if (port === '8081') return `${protocol}//${hostname}:8081`;
            // default: same-origin
            return `${protocol}//${hostname}${port ? `:${port}` : ''}`;
        } catch (_) {
            return '';
        }
    })(),

    // 1. HELPER: Authenticated Fetch (THIS WAS MISSING)
    apiFetch: async (url, options = {}) => {
        const token = localStorage.getItem('jwt_token');
        const headers = {
            ...options.headers
        };

        // If it's not a file upload, default to JSON
        if (!(options.body instanceof FormData)) {
            headers['Content-Type'] = 'application/json';
        }

        // Auto-serialize plain objects to JSON for convenience
        let body = options.body;
        if (body && !(body instanceof FormData) && typeof body !== 'string') {
            try {
                body = JSON.stringify(body);
            } catch (_) {
                // fallback: leave as-is
            }
        }

        if (token) {
            headers['Authorization'] = 'Bearer ' + token;
        }

        // Prepend base for relative URLs
        const finalUrl = url.startsWith('http') ? url : `${$api._apiBase}${url.startsWith('/') ? url : `/${url}`}`;

        const response = await fetch(finalUrl, {
            ...options,
            headers,
            body
        });

        if (response.status === 401) {
            // Token expired or invalid
            $api.logout();
            throw new Error('Session expired');
        }

        return response;
    },

    // 2. LOGIN
    login: async (email, password) => {
        try {
          const response = await $api.apiFetch('/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });
            if (!response.ok) throw new Error('Login failed');
            const data = await response.json();
            localStorage.setItem('jwt_token', data.token);
            localStorage.setItem('user_role', data.role);
            localStorage.setItem('user_name', data.name);
            return true;
        } catch (e) {
            console.error(e);
            return false;
        }
    },

    // 3. REGISTER
    register: async (user) => {
        try {
            const response = await $api.apiFetch('/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(user)
            });
            if (!response.ok) {
                const err = await response.text();
                throw new Error(err);
            }
            return true;
        } catch (e) {
            throw e;
        }
    },

    // 4. LOGOUT
    logout: () => {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_role');
        localStorage.removeItem('user_name');
        window.location.href = '/login.html';
    },

    // 5. CHECK AUTH
    getAuth: () => {
        const token = localStorage.getItem('jwt_token');
        if (!token) return null;
        return {
            token: token,
            role: localStorage.getItem('user_role'),
            name: localStorage.getItem('user_name') || 'User'
        };
    },

    // 6. RENDER HEADER
    renderHeader: (activePage) => {
        const user = $api.getAuth();
        const header = document.createElement('header');
        header.className = 'row';
        header.style.justifyContent = 'space-between';
        header.style.padding = '1rem 2rem';
        header.style.background = '#fff';
        header.style.boxShadow = '0 2px 4px rgba(0,0,0,0.1)';
        header.style.marginBottom = '20px';

        let navLinks = '';

        if (user) {
            if (user.role === 'EMPLOYER') {
                navLinks = `
                    <a href="/post-job.html" class="btn primary">Post a Job</a>
                    <a href="/employer-dashboard.html" class="btn secondary">My Job Posts</a>
                    <a href="/employer-profile.html" class="btn text">My Profile</a>
                    <a href="#" onclick="$api.logout()" class="btn text">Logout (${user.name})</a>
                `;
            } else {
                navLinks = `
                    <a href="/jobs.html" class="btn text">Find Jobs</a>
                    <a href="/my-applications.html" class="btn text">My Applications</a>
                    <a href="/my-profile.html" class="btn text">My Profile</a>
                    <a href="#" onclick="$api.logout()" class="btn text">Logout (${user.name})</a>
                `;
            }
        } else {
            navLinks = `
                <a href="/login.html" class="btn text">Login</a>
                <a href="/register.html" class="btn primary">Register</a>
            `;
        }

        header.innerHTML = `
            <div style="font-weight:bold; font-size:1.5rem; color:#333;">
                <a href="/" style="text-decoration:none; color:inherit;">JobAppSys</a>
            </div>
            <nav class="row" style="gap: 1rem; align-items: center;">${navLinks}</nav>
        `;
        document.body.prepend(header);
    },

    // 7. QUERY STRING PARSER
    qs: (name) => {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }
};