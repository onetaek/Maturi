const searchingInput = document.querySelector('input[name="keywordValue"]');
const radioInputs = document.querySelectorAll('input[name="radioCond"]');
let interLocalContainer = document.querySelector('.input-container-interLocal');
console.log("interLocalContainer",interLocalContainer);

console.log("searchInput",searchInput);
console.log("radioInputs",radioInputs);

searchingInput.addEventListener("input",()=>{
   addPage("click");
});

radioInputs.forEach((radio)=>{
    console.log("radio",radio);
    radio.addEventListener("change",()=>{
        console.log("라디오버튼 change 이벤트 감지");
        addPage("click");
    });
    if(radio.id === "follow" || radio.id === "my-local" || radio.id === "like" || radio.id ==="all"||radio.id==="local"){

    }
});

interLocalContainer.addEventListener("click",()=>{
    console.log("클릭!");
    let local = document.querySelector('label[for="local"]');
    if(local.innerText!=="관심지역"){
        addPage("click");
    }
});



let ssss = document.addEventListener(".radio-title-local");
ssss.addEventListener("click",function(){
    alert("클릭");
})

