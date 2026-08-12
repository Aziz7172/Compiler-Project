from flask import Flask, render_template

app = Flask(__name__)

products = [
    {"id": 1, "name": "Laptop", "price": 1200, "image": "laptop.jpg", "details": "Powerful laptop"},
    {"id": 2, "name": "Phone", "price": 800, "image": "phone.jpg", "details": "Smartphone with good camera"}
]

@app.route("/products")
def show_products():
    return render_template("products.html", products=products)

if (__name__ == "__main__"):
    app.run(debug=True)