class Sidebar extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `
        <h3>I'm sidebar</h3>
        `
    }
}

customElements.define('jm-sidebar', Sidebar)

//How to add:
// <jm-sidebar></jm-sidebar>
// <script type="text/javascript" th:src="@{/js/sidebar.js}"></script>