
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
    window.alert("Tilaus lähetetty!")
    console.log(data);
}
/* Get orders */
async function loadOrders() {
    console.log(url + "customer/orders");
    let response = await fetch(url + "customer/orders", {
        headers: {
            "Accept": "application/json"
        }
    });
    let orders = await response.json();
    console.log(orders);
    addOrdersToPage(orders);
};

async function addOrdersToPage(data) {
    /* Clear elements */
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
        if (order.status === "PENDING") {
            paraStatus.textContent = "Status: Lähetetty";
            acceptButton.className = "disabled-button";
            acceptButton.disabled = true;
            acceptButton.innerText = "Odottaa";
            
        }
        if (order.status === "ACCEPTED") {
            paraStatus.textContent = "Status: Pukki on hyväksynyt";
            acceptButton.className = "disabled-button";
            acceptButton.disabled = true;
            acceptButton.innerText = "Hyväksytty";
        }
        /* Santa info */
        const paraSantaname = document.createElement("p");
        paraSantaname.innerText = order.santaProfile.santaProfileName;

        const paraSantaEmail = document.createElement("p");
        paraSantaEmail.innerText = order.santaProfile.email;

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

        console.log(order.santaProfile);

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
}

/* Delete order */
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
    console.log(data);
    
}

const removeLinkElements = (id) => {
    const parent = document.getElementById(id);
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }
};