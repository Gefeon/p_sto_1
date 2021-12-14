class Sidebar extends HTMLElement {
    connectedCallback() {
        this.innerHTML = `
    <div class="menu sticky-top p-4 bg-light" style="width: 300px; max-width: 300px;">
        <a href="/" class="link-dark">
            <span><h3><small>Home</small></h3></span>
        </a>
        <ul class="list-unstyled pt-3">
            <li class="mb-1">
                <h4>PUBLIC</h4>
                <div class="collapse show">
                    <ul class="list-unstyled fw-normal pb-1 small">
                        <li><a href="#" class="link-dark"><h3><small>Questions</small></h3></a></li>
                        <li><a href="#" class="link-dark"><h3><small>Tags</small></h3></a></li>
                        <li><a href="#" class="link-dark"><h3><small>Users</small></h3></a></li>
                    </ul>
                </div>
            </li>
            <li class="mb-1">
                <h4>COLLECTIVES</h4>
                <div class="collapse show">
                    <ul class="list-unstyled fw-normal pb-1 small">
                        <li><a href="#" class="link-dark"><h3><small>Explore Collectives</small></h3></a></li>
                    </ul>
                </div>
            </li>
            <li class="mb-1">
                <h4>FIND A JOB</h4>
                <div class="collapse show">
                    <ul class="list-unstyled fw-normal pb-1 small">
                        <li><a href="#" class="link-dark"><h3><small>Jobs</small></h3></a></li>
                        <li><a href="#" class="link-dark"><h3><small>Companies</small></h3></a></li>
                    </ul>
                </div>
            </li>
            <li class="mb-1">
                <h4>TEAMS</h4>
                <div class="collapse show">
                    <ul class="list-unstyled pb-1 small">
                        <li><a href="#" class="link-dark"><h3><small>Create a free Team</small></h3></a></li>
                        <li><a href="#" class="link-dark"><h3><small>What is Teams</small></h3></a></li>
                    </ul>
                </div>
            </li>
        </ul>
    </div>
        `
    }
}

customElements.define('jm-sidebar', Sidebar)
