class Header extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `

      <div class="d-flex flex-wrap align-items-center justify-content-around">
        <a href="/" class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
          <img src="/images/page_template/header-logo.svg" alt="logo">
        </a>

        <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
          <li><a href="#" class="nav-link px-2">About</a></li>
          <li><a href="#" class="nav-link px-2">Products</a></li>
          <li><a href="#" class="nav-link px-2">For team</a></li>
        </ul>

        <form class="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3 flex-fill">
          <input type="search" class="form-control form-control-dark" placeholder="Search..." aria-label="Search">
        </form>

        <div class="text-end">
          <button type="button" class="btn btn-primary">Login</button>
          <button type="button" class="btn btn-primary">Sign-up</button>
        </div>
      </div>

        `
    }
}

customElements.define('jm-header', Header)
