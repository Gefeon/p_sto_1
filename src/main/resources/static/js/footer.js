class Footer extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `
        <h3>I'm footer</h3>
        `
    }
}

customElements.define('jm-footer', Footer)

//How to add:
// <jm-footer></jm-footer>
// <script type="text/javascript" th:src="@{/js/footer.js}"></script>