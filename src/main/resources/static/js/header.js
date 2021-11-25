class Header extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `
        <h3>I'm header</h3>
        `
    }
}

customElements.define('jm-header', Header)

//How to add:
// <jm-header></jm-header>
// <script type="text/javascript" th:src="@{/js/header.js}"></script>