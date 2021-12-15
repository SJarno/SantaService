let url = contextRoot;

/* Get santa cards */
async function loadSantas(path) {
    /* document.getElementById("santa-cards").remove(); */
    console.log(url+path);
    let response = await fetch(url+path, {
        headers: {
            "Accept":"application/json"
        }
    });
    let santas = await response.json();
    console.log(santas);
    addToElement(santas);
};

async function loadImageById(id) {
    console.log(url+"santa/image/"+id);
    const response = await fetch(url+"santa/image/"+id, {
        method: "get"
    });
    const image = await response.blob();
    const imageObjectURL = URL.createObjectURL(image);//create local url for image
    return imageObjectURL;
};

const addToElement = data => {
    removeLinkElements("santa-cards");
    data.forEach(santa => {
        console.log(santa);
        
        const divElement = document.createElement("div");
        divElement.className = "santa-card";
        divElement.id = santa.id;

        const headerElement = document.createElement("h3");
        headerElement.innerText = santa.santaProfileName;//hakee santa-profilen kautta profiilin nimen
        const infoPara = document.createElement("p");
        infoPara.innerText = santa.info;

        const pricePara = document.createElement("p");
        pricePara.innerText = "Hinta: "+santa.price;

        const figureElement = document.createElement("figure");
        figureElement.className = "santa-card-figure";
        const imageElement = document.createElement("img");
        
        imageElement.src = "/santa/image/"+santa.id;
        figureElement.appendChild(imageElement);

        const orderButton = document.createElement("button");
        orderButton.textContent = "Lähetä tarjous";
        orderButton.className = "order-button";
        orderButton.onclick = function() {
            sendOffer(santa.id);
        };
        
        divElement.appendChild(figureElement);
        divElement.appendChild(headerElement);
        divElement.appendChild(infoPara);
        divElement.appendChild(pricePara);
        divElement.appendChild(orderButton);

        document.getElementById("santa-cards").appendChild(divElement);
        
    });
};

async function loadTest() {
    console.log(url+"customer/orders");
    let response = await fetch(url+"customer/orders", {
        headers: {
            "Accept":"application/json"
        }
    });
    let santas = await response.json();
    console.log(santas);
};

