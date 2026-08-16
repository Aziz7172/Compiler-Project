from flask import Flask, render_template, request, redirect, url_for

app = Flask(__name__)

products = []

@app.route("/add", methods=["GET", "POST"])
def add_product() {
    if request.method == "POST" {
        new_product = {
            "id": len(products) + 1,
            "name": request.form["name"],
            "price": float(request.form["price"]),
            "image": request.form["image"],
            "details": request.form["details"]
        }
        products.append(new_product)
        return redirect(url_for("show_products"))
    }
    return render_template("add_product.html")

if __name__ == "__main__" {
    app.run(debug=True)
}
