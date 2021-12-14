/* Get orders */
async function loadOrders() {
    console.log(url + "santa/orders");
    let response = await fetch(url + "santa/orders", {
        headers: {
            "Accept": "application/json"
        }
    });
    let orders = await response.json();
    addOrdersToElement(orders);
    console.log(orders);
};

async function addOrdersToElement(data) {
    removeLinkElements("order-cards");
    data.forEach(order => {
        const divElement = document.createElement("div");
        divElement.id = order.id;
        divElement.className = "card-order";
        const header = document.createElement("h3");
        header.innerText = "Tilausnumero: " + order.id;

        const paraStatus = document.createElement("p");
        paraStatus.id = "order-status-"+order.id;
        if (order.status === "PENDING") {
            paraStatus.textContent = "Status: Odottaa hyväksyntää sinulta";
            const acceptButton = document.createElement("button");
            acceptButton.id = "accept-btn-"+order.id;
            acceptButton.innerText = "Hyväksy";
            acceptButton.onclick = function () {
                acceptBySanta(order.id, "ACCEPTED");
            }
            divElement.appendChild(acceptButton);
        }
        if (order.status === "ACCEPTED") {
            paraStatus.textContent = "Status: Hyväksytty";
        }
        /* Add santa info */
        const paraSantaname = document.createElement("p");
        paraSantaname.innerText = "Santa profile name: "+order.santaProfile.profileName;

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


};

async function acceptBySanta(id, status) {
    console.log(url+"santa/orders/update/"+id+"/"+status);
    const response = await fetch(url+"santa/orders/update/"+id+"/"+status, {
        headers: {
            "Accept": "application/json"
        },
        method: "put"
    });
    const data = await response.json();
    const updateElement = document.getElementById("order-status-"+data.id);
    updateElement.innerText = "Status: Hyväksytty";
    const acceptBtn = document.getElementById("accept-btn-"+data.id);
    acceptBtn.remove();
    console.log(data);
}

async function deleteOrder(orderId) {
    console.log(url+"orders/"+orderId+"/delete");
    const response = await fetch(url+"orders/"+orderId+"/delete", {
        headers: {
            "Accept": "application/json"
        },
        method: "delete"
    });
    const data = await response.json();
    document.getElementById(data.id).remove();
    window.alert("Tilaus peruttu")
    
    
}

const removeLinkElements = (id) => {
    const parent = document.getElementById(id);
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }
};