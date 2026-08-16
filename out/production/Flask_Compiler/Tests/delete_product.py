from flask import Flask, redirect, url_for

app = Flask(__name__)

products = [
    {"id": 1, "name": "Laptop", "price": 1200, "image": "laptop.jpg", "details": "Powerful laptop"},
    {"id": 2, "name": "Phone", "price": 800, "image": "phone.jpg", "details": "Smartphone with good camera"}
]

@app.route("/delete/<int:id>")
def delete_product(id) {
    global products
    products = [p for p in products if p["id"] != id]
    return redirect(url_for("show_products"))
}
if __name__ == "__main__" {
    app.run(debug=True)
}
