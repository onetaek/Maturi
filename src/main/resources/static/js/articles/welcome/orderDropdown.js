let orderInputContainer = document.querySelector('#order-wrap');
let orderInput = document.querySelector("#order");
let orderDropListItems = document.querySelectorAll(".order-drop-list li");
let orderLabel = document.querySelector('label[for="order"]');

//드롬다운 메뉴 토글 버튼
orderInputContainer.addEventListener("click",(event)=>{
    event.preventDefault();
    event.stopPropagation();
    orderInput.checked = true;
    let orderDropList = orderInputContainer.querySelector('.order-drop-list');
    if(orderDropList.classList.contains('show')){

    }else{

    }
    orderDropList.classList.toggle('show');
})

orderDropListItems.forEach((item)=>{
    let span = item.querySelector('span');
    item.addEventListener("click",()=>{
        orderLabel.innerText = span.innerText;
    })
})