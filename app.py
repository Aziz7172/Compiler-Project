from flask import Flask, render_template

app = Flask(__name__)

products = [
    {"id": 1, "name": "Widget", "price": 25, "details": "A very useful widget"},
    {"id": 3, "name": "Doohickey", "price": 15, "details": "A cheap and cheerful doohickey"},
    {"id": 4, "name": "llllara", "price": 110, "details": ""},
    {"id": 5, "name": "kjjghhjk", "price": 44, "details": ""}
]
product = {"id": 1, "name": "Widget", "price": 25, "details": "A very useful widget"}
students = [
    {"name": "Alice", "grade": "A"},
    {"name": "Bob", "grade": "B"},
    {"name": "Charlie", "grade": "A"}
]
iterations = [1, 2, 3]
user_name = "Admin"

@app.route("/")
def home() {
    return render_template("index.html", products=products, user_name=user_name)
}

@app.route("/products")
def show_products() {
    return render_template("products.html", products=products, user_name=user_name)
}

@app.route("/product/<name>")
def product_details(name) {
    return render_template("product_details.html", product_name=name, user_name=user_name)
}

if __name__ == "__main__" {
    app.run(debug=True)
}