
/* Create new order: */
async function sendOffer(id) {
    /* console.log("painettu! id on:" + id);
    console.log(url + "customer/" + id + "/create-order"); */
    let response = await fetch(url + "customer/" + id + "/create-order", {
        headers: {
            "Accept": "application/json"
        },
        method: "post"
    });
    let data = await response.json();
    console.log(data);
}
/* Get orders */
async function loadOrders() {
    /*  */
    
    console.log(url + "customer/orders");
    let response = await fetch(url + "customer/orders", {
        headers: {
            "Accept": "application/json"
        }
    });
    let santas = await response.json();
    addOrdersToPage(santas);
};

async function addOrdersToPage(data) {
    removeLinkElements("order-cards");
    data.forEach(order => {
        /* Clear elements */
        /* Create holder for order card */
        const divElement = document.createElement("div");
        divElement.id = order.id;
        divElement.className = "card-order";
        const header = document.createElement("h3");
        header.innerText = "Tilausnumero: " + order.id;

        const paraStatus = document.createElement("p");
        if (order.status === "PENDING") {
            paraStatus.textContent = "Status: Lähetetty";
        }
        /* Add santa info */
        const paraSantaname = document.createElement("p");


        console.log(order.santaProfile);

        /* Add button for deleting order: */
        const deleteOrderButton = document.createElement("button");
        deleteOrderButton.innerHTML = "Peru Tilaus";
        deleteOrderButton.onclick = function () {
            deleteOrder(order.id);
        }

        divElement.appendChild(header);
        divElement.appendChild(paraStatus);
        divElement.appendChild(deleteOrderButton);

        document.getElementById("order-cards").appendChild(divElement);
    });
}

/* Delete order */
async function deleteOrder(orderId) {
    console.log(url+"orders/"+orderId+"/delete");
    const response = await fetch(url+"orders/"+orderId+"/delete", {
        headers: {
            "Accept": "application/json"
        },
        method: "post"
    });
    const data = await response.json();
    document.getElementById(data.id).remove();
    window.alert("Tilaus peruttu")
    console.log(data);
    
}

const removeLinkElements = (id) => {
    const parent = document.getElementById(id);
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }
};