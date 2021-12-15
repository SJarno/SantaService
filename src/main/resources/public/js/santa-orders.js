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
        /* Create holder for order card */
        const divElement = document.createElement("div");
        divElement.id = order.id;
        divElement.className = "card-order";
        const header = document.createElement("h3");
        header.innerText = "Tilausnumero: " + order.id;

        /* button for order status: */
        const acceptButton = document.createElement("button");

        const paraStatus = document.createElement("p");
        paraStatus.id = "order-status-" + order.id;
        /* Update button by status: */
        if (order.status === "PENDING") {
            paraStatus.textContent = "Status: Odottaa hyväksyntää sinulta";
            acceptButton.className = "accept-button";
            acceptButton.id = "accept-btn-" + order.id;
            acceptButton.innerText = "Hyväksy";
            acceptButton.onclick = function () {
                acceptBySanta(order.id, "ACCEPTED");
            }

        }
        if (order.status === "ACCEPTED") {
            paraStatus.textContent = "Status: Hyväksytty";
            acceptButton.className = "disabled-button";
            acceptButton.disabled = true;
            acceptButton.innerText = "Hyväksytty";
        }
        /* Add santa info */
        const paraSantaname = document.createElement("p");
        paraSantaname.innerText = order.santaProfile.santaProfileName;

        const paraSantaEmail = document.createElement("p");
        paraSantaEmail.innerText = order.santaProfile.email;

        console.log(order.santaProfile);

        /* Customer info: */
        const orderInfoHeading = document.createElement("h3");
        orderInfoHeading.innerText = "Tilaajan tiedot";

        const customerProfileNamePara = document.createElement("p");
        customerProfileNamePara.innerText = order.customerProfile.customerProfileName;

        const addressPara = document.createElement("p");
        addressPara.innerText = order.customerProfile.deliveryAddress;

        const postCodePara = document.createElement("p");
        postCodePara.innerText = order.customerProfile.postalCode;

        const customerEmailPara = document.createElement("p");
        customerEmailPara.innerText = order.customerProfile.email;

        /* Add button for deleting order: */
        const deleteOrderButton = document.createElement("button");
        deleteOrderButton.className = "delete-button";
        deleteOrderButton.innerHTML = "Peru Tilaus";
        deleteOrderButton.onclick = function () {
            deleteOrder(order.id);
        }

        divElement.appendChild(acceptButton);
        divElement.appendChild(header);
        divElement.appendChild(paraSantaname)
        divElement.appendChild(paraStatus);
        divElement.appendChild(paraSantaEmail);
        divElement.appendChild(document.createElement("hr"));
        divElement.appendChild(orderInfoHeading);
        divElement.appendChild(customerProfileNamePara);
        divElement.appendChild(addressPara);
        divElement.appendChild(postCodePara);
        divElement.appendChild(customerEmailPara);
        divElement.appendChild(deleteOrderButton);

        document.getElementById("order-cards").appendChild(divElement);
    });


};

async function acceptBySanta(id, status) {
    /* console.log(url + "santa/orders/update/" + id + "/" + status); */
    const acceptBtn = document.getElementById("accept-btn-" + id);
    acceptBtn.innerText = "Lähettää...";
    const response = await fetch(url + "santa/orders/update/" + id + "/" + status, {
        headers: {
            "Accept": "application/json"
        },
        method: "put"
    });
    const data = await response.json();
    const para = document.getElementById("order-status-"+data.id);
    toggleButton(acceptBtn, para, data);
    console.log(data);
}

async function deleteOrder(orderId) {
    console.log(url + "orders/" + orderId + "/delete");
    const response = await fetch(url + "orders/" + orderId + "/delete", {
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

const toggleButton = (button, para,  order) => {
    if (order.status === "PENDING") {
        para.textContent = "Status: Odottaa hyväksyntää sinulta";
        button.className = "accept-button";
        button.id = "accept-btn-" + order.id;
        button.innerText = "Hyväksy";
        button.onclick = function () {
            acceptBySanta(order.id, "ACCEPTED");
        }

    }
    if (order.status === "ACCEPTED") {
        para.textContent = "Status: Hyväksytty";
        button.className = "disabled-button";
        button.disabled = true;
        button.innerText = "Hyväksytty";
    }
}