from flask import Flask, render_template, abort

app = Flask(__name__)

products = [
    {"id": 1, "name": "Laptop", "price": 1200, "image": "laptop.jpg", "details": "Powerful laptop"},
    {"id": 2, "name": "Phone", "price": 800, "image": "phone.jpg", "details": "Smartphone with good camera"}
]

@app.route("/products/<int:id>")
def product_details(id) {
    product = next((p for p in products if p["id"] == id), None)
    if product is None {
        abort(404)
    }
    return render_template("product_details.html", product=product)

if __name__ == "__main__" {
    app.run(debug=True)
}
