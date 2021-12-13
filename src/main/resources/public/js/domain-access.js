let url = contextRoot;

async function loadSantas(path) {
    console.log(url+path);
    let response = await fetch(url+path, {
        headers: {
            "Accept":"application/json"
        }
    });
    let santas = await response.json();
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
    data.forEach(santa => {
        console.log(santa);
        const divElement = document.createElement("div");
        divElement.id = "card";
        const headerElement = document.createElement("h3");
        headerElement.innerText = santa.santaProfileName;//hakee santa-profilen kautta profiilin nimen
        const infoPara = document.createElement("p");
        infoPara.innerText = santa.info;

        const pricePara = document.createElement("p");
        pricePara.innerText = "Hinta: "+santa.price;

        const figureElement = document.createElement("figure");
        figureElement.id = "card-figure";
        const imageElement = document.createElement("img");
        console.log("/santa/image/"+santa.id);
        
        imageElement.src = "/santa/image/"+santa.id;
        figureElement.appendChild(imageElement);

        const testButton = document.createElement("button");
        testButton.textContent = "Paina";
        testButton.onclick = function() {
            sendOffer();
        };
        
        divElement.appendChild(figureElement);
        divElement.appendChild(headerElement);
        divElement.appendChild(infoPara);
        divElement.appendChild(pricePara);
        divElement.appendChild(testButton);

        document.getElementById("santa-cards").appendChild(divElement);
        
    });
};

async function loadTest() {
    /* console.log(url+"testi"); */
    let response = await fetch(url+"santas/available", {
        headers: {
            "Accept":"application/json"
        }
    });
    let santas = await response.json();
    console.log(santas);
};

async function sendOffer() {
    console.log("painettu!");
}