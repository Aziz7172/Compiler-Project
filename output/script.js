/**
 * ============================================================================
 *  script.js — Client-side interactivity for the Flask_Compiler static output
 * ============================================================================
 *  This file lives in the PROJECT ROOT and is copied into output/ by the Java
 *  compiler (TemplateManager.copyStaticAssets()). Every generated page loads
 *  it at the very bottom of <body>:
 *
 *      <script src="script.js"></script>
 *
 *  ── WHAT THIS SCRIPT DOES ──────────────────────────────────────────────────
 *  1. Waits for the DOM to be ready (DOMContentLoaded).
 *  2. HTTP mode (DevServer): rows are server-rendered and add/delete go through
 *     the /api endpoints — the client only wires delete/search/form events.
 *  3. FILE mode (file://): seeds localStorage with default products the very
 *     first time the app runs, so the table is never empty.
 *  4. Reads the products from localStorage and DYNAMICALLY renders every
 *     <tr> into <tbody id="tableBody">. localStorage is the single source of
 *     truth — the static rows emitted by Jinja are intentionally ignored so
 *     the UI always matches the stored data.
 *  5. Binds delete / edit / search / form events AFTER the first render.
 *  6. Delete  → fade-out the row, update localStorage, re-render (file mode).
 *  7. Search  → filters visible rows as the user types in #searchInput.
 *  8. Forms   → save (add) or update (edit) in localStorage (file mode), or
 *               POST to /api/add (HTTP mode), then redirect to products.html.
 *  9. Edit page → pre-fills the form from localStorage via ?id= query param.
 *
 *  Pure Vanilla JavaScript. No jQuery. console.log() at every step so you can
 *  trace execution in the browser DevTools (F12).
 * ============================================================================
 */
document.addEventListener('DOMContentLoaded', function () {
    'use strict';

    /* ══════════════════════════════════════════════════════════════════════
     *  0. CONFIG + DOM REFERENCES
     * ══════════════════════════════════════════════════════════════════════ */
    console.log('[app] DOM Loaded — script.js is running.');

    var STORAGE_KEY = 'products';          // localStorage key (plural form)
    var ENTITY      = 'product';           // singular entity name

    // Progressive enhancement: over http:// (DevServer) the server is the
    // source of truth and renders the rows itself — add/delete go through the
    // API. Over file:// we fall back to the localStorage-only mode below.
    var IS_HTTP = (window.location.protocol === 'http:' || window.location.protocol === 'https:');

    // Default seed data — only used the very first time the app runs.
    var DEFAULT_PRODUCTS = [
        { id: 1, name: 'Widget',    price: 25, image: '', details: 'A very useful widget' },
        { id: 2, name: 'Gadget',    price: 45, image: '', details: 'An amazing new gadget' },
        { id: 3, name: 'Doohickey', price: 15, image: '', details: 'A cheap and cheerful doohickey' }
    ];

    var tableBody   = document.getElementById('tableBody');   // <tbody> for rows
    var searchInput = document.getElementById('searchInput'); // the search box

    /* ══════════════════════════════════════════════════════════════════════
     *  1. LOCALSTORAGE HELPERS
     * ══════════════════════════════════════════════════════════════════════ */

    /** Read all products from localStorage. Never throws — returns [] on error. */
    function readProducts() {
        try {
            var raw = localStorage.getItem(STORAGE_KEY);
            var list = raw ? JSON.parse(raw) : [];
            return Array.isArray(list) ? list : [];
        } catch (err) {
            console.error('[storage] Failed to parse "' + STORAGE_KEY + '":', err);
            return [];
        }
    }

    /** Persist the full products array back to localStorage. */
    function writeProducts(list) {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(list));
        console.log('[storage] Saved ' + list.length + ' product(s) to "' + STORAGE_KEY + '".');
    }

    /**
     * Seed defaults when localStorage is empty (first ever visit).
     * Guarantees the app never starts with an empty table.
     */
    function ensureInitialized() {
        if (localStorage.getItem(STORAGE_KEY) === null) {
            console.log('[init] No "' + STORAGE_KEY + '" found — seeding default products.');
            writeProducts(DEFAULT_PRODUCTS);
        } else {
            console.log('[init] "' + STORAGE_KEY + '" already exists in localStorage.');
        }
    }

    /** Compute the next numeric id (max id + 1) so new rows never collide. */
    function nextId(list) {
        return list.reduce(function (max, item) {
            var id = Number(item.id) || 0;
            return id > max ? id : max;
        }, 0) + 1;
    }

    /** Escape user-entered text so it can never break out of the DOM. */
    function escapeHtml(value) {
        if (value === null || value === undefined) { return ''; }
        return String(value)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;');
    }

    /* ══════════════════════════════════════════════════════════════════════
     *  2. DYNAMIC RENDERING  (CRUCIAL — localStorage is the source of truth)
     * ══════════════════════════════════════════════════════════════════════ */

    /** (Re)build every <tr> inside <tbody id="tableBody"> from localStorage. */
    function renderTable() {
        if (!tableBody) {
            console.warn('[render] No #tableBody found on this page — skipping table render.');
            return;
        }

        var list = readProducts();
        console.log('[render] Rendering ' + list.length + ' product row(s) into #tableBody.');

        tableBody.innerHTML = '';          // wipe any static/old rows first

        list.forEach(function (product) {
            var tr = document.createElement('tr');
            tr.setAttribute('data-id', product.id);
            tr.setAttribute('data-entity', ENTITY);

            tr.innerHTML =
                '<td>' + escapeHtml(product.id) + '</td>' +
                '<td>' + escapeHtml(product.name) + '</td>' +
                '<td>$' + escapeHtml(product.price) + '</td>' +
                '<td>' + escapeHtml(product.details) + '</td>' +
                '<td>' +
                    '<a class="btn btn-view"   href="product_details.html?id=' + product.id + '">View</a>' +
                    '<a class="btn btn-edit"   href="edit_product.html?id=' + product.id + '" data-entity="' + ENTITY + '" data-id="' + product.id + '">Edit</a>' +
                    '<a class="btn btn-delete" href="#" data-entity="' + ENTITY + '" data-id="' + product.id + '">Delete</a>' +
                '</td>';

            tableBody.appendChild(tr);
        });

        console.log('[render] Found ' + document.querySelectorAll('.btn-delete').length +
            ' delete button(s) on the page.');
    }

    /**
     * (Re)populate the product details page (product_details.html?id=N) from
     * localStorage. The static HTML ships with placeholder content, so this
     * overrides the name/price/details and fixes the Edit/Delete links to
     * point at THIS product (id 0 is only a stand-in).
     */
    function renderDetails() {
        var nameEl = document.getElementById('detailName');
        if (!nameEl) {
            console.log('[details] No #detailName on this page — not a details page.');
            return;
        }

        var urlId    = getUrlParam('id');
        var product  = urlId !== null ? findById(readProducts(), urlId) : null;
        var priceEl  = document.getElementById('detailPrice');
        var detailsEl = document.getElementById('detailDetails');

        if (!product) {
            console.warn('[details] Product id ' + urlId + ' not found — showing fallback.');
            nameEl.textContent = 'Product not found';
            if (priceEl)   { priceEl.textContent = ''; }
            if (detailsEl) { detailsEl.textContent = ''; }
            return;
        }

        console.log('[details] Showing product id ' + product.id + ' (' + product.name + ').');
        document.title = product.name;
        nameEl.textContent = product.name;
        if (priceEl)   { priceEl.textContent = '$' + product.price; }
        if (detailsEl) { detailsEl.textContent = product.details || ''; }

        var editBtn   = document.querySelector('.btn-edit');
        var deleteBtn = document.querySelector('.btn-delete');
        if (editBtn)   { editBtn.setAttribute('data-id', product.id); editBtn.href = 'edit_product.html?id=' + product.id; }
        if (deleteBtn) { deleteBtn.setAttribute('data-id', product.id); deleteBtn.href = 'index.html?delete=' + product.id; }
    }

    /* ══════════════════════════════════════════════════════════════════════
     *  3. DELETE
     * ══════════════════════════════════════════════════════════════════════ */

    /** Remove a product from localStorage by id. Returns true if removed. */
    function deleteProduct(id) {
        var list = readProducts();
        var before = list.length;
        var filtered = list.filter(function (p) {
            return String(p.id) !== String(id);   // string compare avoids "1" vs 1
        });
        if (filtered.length === before) {
            console.warn('[delete] No product with id ' + id + ' was found in storage.');
            return false;
        }
        writeProducts(filtered);
        return true;
    }

    /* ══════════════════════════════════════════════════════════════════════
     *  4. EVENT BINDING  (AFTER the first render, so every row is live)
     * ══════════════════════════════════════════════════════════════════════ */

    function bindEvents() {

        /* ── DELETE (document-level delegation → also catches rows that were
         *    added by renderTable, and the delete button on the edit page) ── */
        document.addEventListener('click', function (e) {
            var deleteBtn = e.target.closest('.btn-delete');
            if (!deleteBtn) { return; }

            e.preventDefault();
            var id = deleteBtn.getAttribute('data-id');
            console.log('[delete] Delete clicked for ID ' + id);

            if (!confirm('Delete this product (id ' + id + ')?')) {
                console.log('[delete] Deletion cancelled by the user.');
                return;
            }

            if (IS_HTTP) {
                // Server mode: the delete endpoint rewrites app.py, regenerates
                // the site and 303-redirects back to /products.html.
                console.log('[delete] HTTP mode -> GET /api/delete?id=' + id);
                window.location.href = '/api/delete?id=' + encodeURIComponent(id);
                return;
            }

            if (!deleteProduct(id)) { return; }

            var row = deleteBtn.closest('tr');
            if (row) {
                // List page: fade the row out, then re-render from storage.
                row.style.transition = 'opacity 0.3s ease';
                row.style.opacity = '0';
                console.log('[delete] Fading out row for ID ' + id + '.');
                setTimeout(function () {
                    renderTable();
                    console.log('[delete] Table re-rendered after deleting id ' + id + '.');
                }, 300);
            } else {
                // Edit page: there is no table row — go back to the list.
                console.log('[delete] Deleted from the edit page — returning to products.html.');
                window.location.href = 'products.html';
            }
        });

        /* ── EDIT (log only — the default <a href="edit_product.html?id=N"> is
         *    allowed to navigate normally) ── */
        document.addEventListener('click', function (e) {
            var editBtn = e.target.closest('.btn-edit');
            if (!editBtn) { return; }
            console.log('[edit] Edit clicked for ID ' + editBtn.getAttribute('data-id'));
        });

        /* ── SEARCH (filter visible rows as the user types in #searchInput) ── */
        if (searchInput) {
            searchInput.addEventListener('input', function () {
                var q = searchInput.value.toLowerCase().trim();
                console.log('[search] Filtering table for "' + searchInput.value + '".');

                if (!tableBody) { return; }
                var rows = tableBody.querySelectorAll('tr');
                rows.forEach(function (row) {
                    var matches = row.textContent.toLowerCase().indexOf(q) !== -1;
                    row.style.display = matches ? '' : 'none';
                });
            });
            console.log('[search] Bound #searchInput to the table filter.');
        } else {
            console.log('[search] No #searchInput on this page — search not enabled.');
        }

        /* ── FORM (add page: #productForm, edit page: .generic-form) ── */
        var form = document.getElementById('productForm') || document.querySelector('.generic-form');
        if (form) {
            form.addEventListener('submit', function (e) {
                e.preventDefault();
                console.log('[form] Submit intercepted on ' + (form.id || 'a generic-form') + '.');

                if (IS_HTTP) {
                    var id = form.getAttribute('data-id');
                    if (id) {
                        // Edit page: no server-side edit endpoint yet — don't
                        // destroy server data, just go back to the list.
                        console.log('[form] Edit over http API not wired — returning to products.html.');
                        window.location.href = 'products.html';
                    } else {
                        // Add page: hand off to /api/add (server rewrites app.py,
                        // regenerates, then 303-redirects to products.html).
                        console.log('[form] HTTP mode -> POST /api/add.');
                        form.action = '/api/add';
                        form.method = 'post';
                        e.preventDefault();
                        form.submit();
                    }
                    return;
                }

                var id   = form.getAttribute('data-id');
                var list = readProducts();

                var record = {
                    name:    (form.querySelector('[name="name"]')    || {}).value || '',
                    price:   Number((form.querySelector('[name="price"]')   || {}).value) || 0,
                    image:   (form.querySelector('[name="image"]')   || {}).value || '',
                    details: (form.querySelector('[name="details"]') || {}).value || ''
                };

                if (id) {
                    // ── EDIT mode: update the existing record in place ──
                    var matched = false;
                    for (var i = 0; i < list.length; i++) {
                        if (String(list[i].id) === String(id)) {
                            record.id = list[i].id;
                            list[i] = record;
                            matched = true;
                            console.log('[form] Updated product id ' + id + '.');
                            break;
                        }
                    }
                    if (!matched) {
                        record.id = Number(id);
                        list.push(record);
                        console.log('[form] Re-created product id ' + id + '.');
                    }
                } else {
                    // ── ADD mode: assign a fresh id and append ──
                    record.id = nextId(list);
                    list.push(record);
                    console.log('[form] Added new product id ' + record.id + '.');
                }

                writeProducts(list);
                console.log('[form] Redirecting to products.html...');
                window.location.href = 'products.html';
            });
            console.log('[form] Bound submit handler to the product form.');
        } else {
            console.log('[form] No product form on this page.');
        }

        /* ── EDIT PAGE PRE-FILL ──
         * On edit_product.html?id=N, load that product from localStorage and
         * populate the form. localStorage is the source of truth, so this
         * overrides whatever the static HTML rendered. */
        var urlId = getUrlParam('id');
        if (!IS_HTTP && urlId !== null && form) {
            var target = findById(readProducts(), urlId);
            if (target) {
                form.setAttribute('data-id', target.id);
                setField(form, 'name', target.name);
                setField(form, 'price', target.price);
                setField(form, 'image', target.image);
                setField(form, 'details', target.details);
                console.log('[edit-page] Pre-filled form with product id ' + target.id + '.');
            } else {
                console.warn('[edit-page] No product with id ' + urlId + ' found in localStorage.');
            }
        }
    }

    /* ── small helpers used by bindEvents() ── */

    /** Read a query-string parameter (null when absent). */
    function getUrlParam(name) {
        return new URLSearchParams(window.location.search).get(name);
    }

    /** Find a product in a list by id (string-safe comparison). */
    function findById(list, id) {
        for (var i = 0; i < list.length; i++) {
            if (String(list[i].id) === String(id)) { return list[i]; }
        }
        return null;
    }

    /** Set the value of a named field inside a form. */
    function setField(form, name, value) {
        var el = form.querySelector('[name="' + name + '"]');
        if (el) { el.value = (value === null || value === undefined) ? '' : value; }
    }

    /* ══════════════════════════════════════════════════════════════════════
     *  5. BOOTSTRAP — THE EXACT ORDER THAT MAKES EVERYTHING WORK:
     *     1. seed localStorage    2. render rows     3. THEN bind events
     * ══════════════════════════════════════════════════════════════════════ */
    // Over http:// the DevServer renders the rows and owns add/delete, so we
    // skip seeding and keep the server-rendered table. Over file:// we use the
    // localStorage source-of-truth flow below.
    if (IS_HTTP) {
        console.log('[app] HTTP mode — keeping server-rendered rows, wiring events.');
        bindEvents();
    } else {
        ensureInitialized();   // 1. seed default products if storage is empty
        renderTable();         // 2. build all <tr> from localStorage
        renderDetails();       // 3. (re)populate the details page from localStorage
        bindEvents();          // 4. wire delete / edit / search / form listeners
    }
    console.log('[app] Bootstrap complete. Interactivity enabled.');
});
