console.log("script loaded");

let currentTheme = getTheme();

document.addEventListener("DOMContentLoaded", () => {
    // Initialize theme
    changeTheme();

    // Initialize sidebar toggle
    setupSidebarToggle();

    // Disable Flowbite drawer for #logo-sidebar
    const sidebar = document.getElementById('logo-sidebar');
    if (sidebar) {
        sidebar.removeAttribute('data-drawer');
        sidebar.removeAttribute('data-drawer-target');
        sidebar.removeAttribute('data-drawer-toggle');
        console.log("Flowbite drawer attributes removed from #logo-sidebar");
    }
});

// Theme switching functionality
function changeTheme() {
    document.querySelector("html").classList.add(currentTheme);

    const changeThemeButton = document.querySelector('#theme-change-button');
    if (changeThemeButton) {
        changeThemeButton.addEventListener('click', (event) => {
            console.log("Theme change button clicked");
            const oldTheme = currentTheme;
            if (currentTheme === "light") {
                currentTheme = "dark";
            } else {
                currentTheme = "light";
            }
            setTheme(currentTheme);
            document.querySelector("html").classList.remove(oldTheme);
            document.querySelector("html").classList.add(currentTheme);
            updateButtonText(currentTheme);
        });
    } else {
        console.warn("Theme change button (#theme-change-button) not found");
    }
}

function updateButtonText(theme) {
    const changeThemeButton = document.querySelector('#theme-change-button');
    if (changeThemeButton) {
        changeThemeButton.querySelector("span").textContent = theme === "dark" ? "Light" : "Dark";
    }
}

function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

function getTheme() {
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light";
}

// Sidebar toggle functionality
function setupSidebarToggle() {
    const sidebar = document.querySelector('#logo-sidebar');
    const openButton = document.getElementById('sidebar-open-toggle');
    const closeButton = document.getElementById('sidebar-close-toggle');
    const backdrop = document.getElementById('sidebar-backdrop');

    if (!sidebar || !openButton || !closeButton || !backdrop) {
        console.error('One or more elements not found:', {
            sidebar: !!sidebar,
            openButton: !!openButton,
            closeButton: !!closeButton,
            backdrop: !!backdrop
        });
        return;
    }

    // Initialize sidebar state
    if (window.innerWidth < 640) {
        sidebar.style.transform = 'translateX(-100%)';
        backdrop.style.display = 'none';
        openButton.classList.remove('hidden');
    } else {
        sidebar.style.transform = 'translateX(0)';
        backdrop.style.display = 'none';
        openButton.classList.add('hidden');
    }

    // Open sidebar function
    const openSidebar = () => {
        console.log('Opening sidebar');
        sidebar.style.transform = 'translateX(0)';
        backdrop.style.display = 'block';
        backdrop.style.opacity = '1';
        openButton.classList.add('hidden');
    };

    // Close sidebar function
    const closeSidebar = () => {
        console.log('Closing sidebar');
        sidebar.style.transform = 'translateX(-100%)';
        backdrop.style.opacity = '0';
        setTimeout(() => {
            backdrop.style.display = 'none';
        }, 300); // Match transition duration
        openButton.classList.remove('hidden');
    };

    // Add event listeners
    openButton.addEventListener('click', openSidebar);
    closeButton.addEventListener('click', closeSidebar);
    backdrop.addEventListener('click', closeSidebar);

    // Handle window resize
    window.addEventListener('resize', () => {
        if (window.innerWidth >= 640) {
            sidebar.style.transform = 'translateX(0)';
            backdrop.style.display = 'none';
            openButton.classList.add('hidden');
        } else {
            if (sidebar.style.transform !== 'translateX(0)') {
                sidebar.style.transform = 'translateX(-100%)';
                backdrop.style.display = 'none';
                openButton.classList.remove('hidden');
            }
        }
    });
}