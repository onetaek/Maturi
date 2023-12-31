const searchingInput = document.querySelector('input[name="keywordValue"]');
const radioInputs = document.querySelectorAll('input[name="radioCond"]');
let interLocalContainer = document.querySelector('.input-container-interLocal');

searchingInput.addEventListener("input",()=>{
   addPage("click");
});

radioInputs.forEach((radio)=>{
    radio.addEventListener("change",()=>{
        addPage("click");
    });
    if(radio.id === "follow" || radio.id === "my-local" || radio.id === "like" || radio.id ==="all"||radio.id==="local"){

    }
});

interLocalContainer.addEventListener("click",()=>{
    let local = document.querySelector('label[for="local"]');
    if(local.innerText!=="관심지역"){
        addPage("click");
    }
});

