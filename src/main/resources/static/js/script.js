console.log("Script loaded");

// Theme management
let currentTheme = getStoredTheme();

function initTheme() {
    applyTheme(currentTheme);
    updateThemeIcon(currentTheme);
    setupThemeToggle();
}

function setupThemeToggle() {
    const themeButton = document.getElementById('theme-change-button');
    if (themeButton) {
        themeButton.addEventListener('click', toggleTheme);
    }
}

function toggleTheme() {
    currentTheme = currentTheme === 'light' ? 'dark' : 'light';
    setStoredTheme(currentTheme);
    applyTheme(currentTheme);
    updateThemeIcon(currentTheme);
}

function applyTheme(theme) {
    const html = document.documentElement;
    html.classList.remove('light', 'dark');
    html.classList.add(theme);
}

function updateThemeIcon(theme) {
    const icon = document.getElementById('theme-icon');
    if (icon) {
        icon.className = theme === 'dark' ? 'fa-solid fa-moon' : 'fa-solid fa-sun';
    }
}

function getStoredTheme() {
    return localStorage.getItem('theme') || 'light';
}

function setStoredTheme(theme) {
    localStorage.setItem('theme', theme);
}

// Sidebar management
function initSidebar() {
    if (!window.loggedInUser) {
        hideSidebarElements();
        return;
    }

    setupSidebarElements();
    handleResize();
    window.addEventListener('resize', handleResize);
}

function hideSidebarElements() {
    const elements = ['logo-sidebar', 'sidebar-open-toggle', 'sidebar-backdrop'];
    elements.forEach(id => {
        const el = document.getElementById(id);
        if (el) el.style.display = 'none';
    });
}

function setupSidebarElements() {
    const sidebar = document.getElementById('logo-sidebar');
    const openBtn = document.getElementById('sidebar-open-toggle');
    const closeBtn = document.getElementById('sidebar-close-toggle');
    const backdrop = document.getElementById('sidebar-backdrop');

    if (!sidebar || !openBtn || !closeBtn || !backdrop) return;

    openBtn.addEventListener('click', openSidebar);
    closeBtn.addEventListener('click', closeSidebar);
    backdrop.addEventListener('click', closeSidebar);
}

function handleResize() {
    if (!window.loggedInUser) {
        hideSidebarElements();
        return;
    }

    const sidebar = document.getElementById('logo-sidebar');
    const openBtn = document.getElementById('sidebar-open-toggle');
    const backdrop = document.getElementById('sidebar-backdrop');

    if (window.innerWidth >= 640) {
        // Desktop
        if (sidebar) {
            sidebar.style.transform = 'translateX(0)';
            sidebar.style.pointerEvents = 'auto';
            sidebar.style.zIndex = '40';
        }
        if (openBtn) openBtn.classList.add('hidden');
        if (backdrop) backdrop.style.display = 'none';
    } else {
        // Mobile
        if (sidebar && sidebar.style.transform !== 'translateX(0)') {
            sidebar.style.transform = 'translateX(-100%)';
            sidebar.style.pointerEvents = 'none';
            sidebar.style.zIndex = '-1';
        }
        if (openBtn) openBtn.classList.remove('hidden');
        if (backdrop) backdrop.style.display = 'none';
    }
}

function openSidebar() {
    const sidebar = document.getElementById('logo-sidebar');
    const openBtn = document.getElementById('sidebar-open-toggle');
    const backdrop = document.getElementById('sidebar-backdrop');

    if (sidebar) {
        sidebar.style.transform = 'translateX(0)';
        sidebar.style.pointerEvents = 'auto';
        sidebar.style.zIndex = '40';
    }
    if (openBtn) openBtn.classList.add('hidden');
    if (backdrop) {
        backdrop.style.display = 'block';
        backdrop.style.opacity = '1';
    }
}

function closeSidebar() {
    const sidebar = document.getElementById('logo-sidebar');
    const openBtn = document.getElementById('sidebar-open-toggle');
    const backdrop = document.getElementById('sidebar-backdrop');

    if (sidebar) {
        sidebar.style.transform = 'translateX(-100%)';
        sidebar.style.pointerEvents = 'none';
        sidebar.style.zIndex = '-1';
    }
    if (openBtn) openBtn.classList.remove('hidden');
    if (backdrop) {
        backdrop.style.opacity = '0';
        setTimeout(() => backdrop.style.display = 'none', 300);
    }
}

// Blog modal functions
function openWriteModal() {
    document.getElementById('writeModal').classList.remove('hidden');
}

function closeWriteModal() {
    document.getElementById('writeModal').classList.add('hidden');
}

function submitBlogPost(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);

    // Validate required fields
    if (!formData.get('title') || !formData.get('content')) {
        alert('Title and content are required.');
        return;
    }

    // Show loading state
    const submitButton = form.querySelector('button[type="submit"]');
    const originalText = submitButton.textContent;
    submitButton.textContent = 'Submitting...';
    submitButton.disabled = true;

    fetch('/doctor/blog/create', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to create blog post');
        }
        return response.json();
    })
    .then(result => {
        if (result.success) {
            alert('Blog post created successfully!');
            closeWriteModal();
            form.reset();
            // Optionally reload or update blog list
            window.location.reload();
        } else {
            alert('Error: ' + (result.message || 'Unknown error'));
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error creating blog post: ' + error.message);
    })
    .finally(() => {
        // Reset button state
        submitButton.textContent = originalText;
        submitButton.disabled = false;
    });
}

// Initialization
document.addEventListener('DOMContentLoaded', () => {
    initTheme();
    initSidebar();
});
