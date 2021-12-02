class Sidebar extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `
<div>
    <a href="/" class="link-dark">
      <span>Home</span>
    </a>
    <ul class="list-unstyled pt-3">
      <li class="mb-1">
          PUBLIC
        <div class="collapse show">
          <ul class="list-unstyled fw-normal pb-1 small">
            <li><a href="#" class="link-dark">Questions</a></li>
            <li><a href="#" class="link-dark">Tags</a></li>
            <li><a href="#" class="link-dark">Users</a></li>
          </ul>
        </div>
      </li>
      <li class="mb-1">
          COLLECTIVES
        <div class="collapse show">
          <ul class="list-unstyled fw-normal pb-1 small">
            <li><a href="#" class="link-dark">Explore Collectives</a></li>
          </ul>
        </div>
      </li>
      <li class="mb-1">
          FIND A JOB
        <div class="collapse show">
          <ul class="list-unstyled fw-normal pb-1 small">
            <li><a href="#" class="link-dark">Jobs</a></li>
            <li><a href="#" class="link-dark">Companies</a></li>
          </ul>
        </div>
      </li>
      <li class="mb-1">
          TEAMS
        <div class="collapse show">
          <ul class="list-unstyled pb-1 small">
            <li><a href="#" class="link-dark">Create a free Team</a></li>
            <li><a href="#" class="link-dark">What is Teams</a></li>
          </ul>
        </div>
      </li>
      
    </ul>
  </div>
        `
    }
}

customElements.define('jm-sidebar', Sidebar)
