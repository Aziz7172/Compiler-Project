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
 *  2. Seeds localStorage with default products the very first time the app
 *     runs, so the table is never empty.
 *  3. Reads the products from localStorage and DYNAMICALLY renders every
 *     <tr> into <tbody id="tableBody">. localStorage is the single source of
 *     truth — the static rows emitted by Jinja are intentionally ignored so
 *     the UI always matches the stored data.
 *  4. Binds delete / edit / search / form events AFTER the first render.
 *  5. Delete  → fade-out the row, update localStorage, re-render.
 *  6. Search  → filters visible rows as the user types in #searchInput.
 *  7. Forms   → save (add) or update (edit) in localStorage, then redirect
 *               back to products.html.
 *  8. Edit page → pre-fills the form from localStorage via ?id= query param.
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

    var STORAGE_KEY    = 'products';        // localStorage key (plural form)
    var BUILD_ID_KEY   = 'products_build_id';  // compiler build that last seeded storage
    var ENTITY         = 'product';         // singular entity name

    // Fallback seed data — only used if the compiler-rendered static rows
    // are absent (e.g. the template has no {% for %} loop over products).
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
     * Harvest the static rows the Java compiler rendered from app.py
     * (templates/products.jinja loops over the `products` context).
     * Returns an array of product objects, or [] if none found.
     */
    function harvestStaticRows() {
        if (!tableBody) { return []; }
        var harvested = [];
        tableBody.querySelectorAll('tr[data-id]').forEach(function (tr) {
            var tds = tr.querySelectorAll('td');
            if (tds.length < 4) { return; }
            harvested.push({
                id:      Number(tr.getAttribute('data-id')) || Number((tds[0].textContent || '').replace(/[^0-9.]/g, '')) || 0,
                name:    (tds[1] ? tds[1].textContent : '').trim(),
                price:   Number(((tds[2] ? tds[2].textContent : '')).replace(/[^0-9.]/g, '')) || 0,
                image:   '',
                details: (tds[3] ? tds[3].textContent : '').trim()
            });
        });
        return harvested;
    }

    /**
     * Read the JSON payload the Java compiler injects into every generated
     * page (<script id="compiled-context" type="application/json">).
     * Contains {"buildId": "...", "products": [...]} straight from app.py.
     * Returns null when the page has no payload (e.g. opened via file://
     * from an older build).
     */
    function readCompiledPayload() {
        var el = document.getElementById('compiled-context');
        if (!el) { return null; }
        try {
            return JSON.parse(el.textContent);
        } catch (err) {
            console.error('[init] Failed to parse #compiled-context payload:', err);
            return null;
        }
    }

    /**
     * Seed / re-seed localStorage from the compiled app.py data.
     *
     * The compiler stamps every generated page with a unique buildId and the
     * compiled `products` list. Whenever the page's buildId differs from the
     * one recorded in localStorage, the compiler has re-run — so storage is
     * RE-SEEDED from app.py, restoring deleted products and dropping stale
     * ones. Between builds, user edits (add/delete/edit) are preserved.
     */
    function ensureInitialized() {
        var payload = readCompiledPayload();
        var compiledProducts = (payload && Array.isArray(payload.products)) ? payload.products : null;
        var buildId = (payload && payload.buildId) ? payload.buildId : null;
        var storedBuildId = localStorage.getItem(BUILD_ID_KEY);

        if (buildId !== null && buildId !== storedBuildId) {
            // ── New compiler build detected → restore the compiled state ──
            if (compiledProducts !== null) {
                console.log('[init] New build ' + buildId + ' detected (previous: ' + storedBuildId +
                    ') — re-seeding ' + compiledProducts.length + ' product(s) from app.py.');
                writeProducts(compiledProducts);
            } else if (harvestStaticRows().length > 0) {
                var rows = harvestStaticRows();
                console.log('[init] New build detected, no JSON payload — seeding ' + rows.length +
                    ' product(s) from the static table.');
                writeProducts(rows);
            } else {
                console.log('[init] New build detected, no compiled data — seeding default products.');
                writeProducts(DEFAULT_PRODUCTS);
            }
            localStorage.setItem(BUILD_ID_KEY, buildId);
        } else if (localStorage.getItem(STORAGE_KEY) === null) {
            // ── First ever visit (or storage cleared) ──
            if (compiledProducts !== null) {
                console.log('[init] No "' + STORAGE_KEY + '" found — seeding ' + compiledProducts.length +
                    ' product(s) from the compiled app.py data.');
                writeProducts(compiledProducts);
            } else if (harvestStaticRows().length > 0) {
                var staticRows = harvestStaticRows();
                console.log('[init] No "' + STORAGE_KEY + '" found — seeding ' + staticRows.length +
                    ' product(s) from the static table.');
                writeProducts(staticRows);
            } else {
                console.log('[init] No "' + STORAGE_KEY + '" found and no compiled data — seeding default products.');
                writeProducts(DEFAULT_PRODUCTS);
            }
            if (buildId !== null) { localStorage.setItem(BUILD_ID_KEY, buildId); }
        } else {
            console.log('[init] "' + STORAGE_KEY + '" exists and build ' + (buildId || '?') +
                ' already applied — keeping user edits.');
        }
    }

    /** Compute the next numeric id (max id + 1) so new rows never collide. */
    function nextId(list) {
        return list.reduce(function (max, item) {
            var id = Number(item.id) || 0;
            return id > max ? id : max;
        }, 0) + 1;
    }

    /**
     * Live-update dashboard stat cards from localStorage.
     * The compiler bakes {{ products|length }} into the static HTML at
     * compile time; this overrides it at runtime so the "Total Products"
     * stat always reflects the ACTUAL number of products (including
     * user adds/deletes), in real time.
     */
    function renderStats() {
        var statEl = document.getElementById('statTotalProducts');
        if (!statEl) { return; }   // not the dashboard page
        var count = readProducts().length;
        statEl.textContent = String(count);
        console.log('[stats] Total Products stat updated to ' + count + '.');
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

            if (!deleteProduct(id)) { return; }
            renderStats();   // keep dashboard stat in sync after delete

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
                renderStats();   // keep dashboard stat in sync after add/edit
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
        if (urlId !== null && form) {
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
    ensureInitialized();   // 1. seed default products if storage is empty
    renderTable();         // 2. build all <tr> from localStorage
    renderDetails();       // 3. (re)populate the details page from localStorage
    renderStats();         // 4. sync dashboard stat cards with localStorage
    bindEvents();          // 5. wire delete / edit / search / form listeners
    console.log('[app] Bootstrap complete. Interactivity enabled.');
});
