let categoryDropList = document.querySelector('.category-drop-list');
let inputContainerCategory = document.querySelector('.input-container-category');
let categoryDropListItems = document.querySelectorAll('.input-container-category .category-drop-list li');
let categoryLabel = document.querySelector('label[for="category"]');

inputContainerCategory.addEventListener("mouseenter",(e)=>{
    categoryDropList.style.height=`${categoryDropListItems.length * categoryDropListItems[0].clientHeight+20}px`;
});

categoryDropList.addEventListener("mouseleave",()=>{
    categoryDropList.style.height=0;
});

inputContainerCategory.addEventListener("mouseleave",()=>{
    categoryDropList.style.height=0;
});

categoryDropListItems.forEach((item)=>{
   item.addEventListener("click",()=>{
       console.log(item.innerText);
       categoryLabel = document.querySelector('label[for="category"]');
       if(item.innerText==="선택안함"){
           categoryLabel.innerText = "카테고리";
           $("#all").prop("checked",true);
       }else{
        categoryLabel.innerText = item.innerText;
           $("#category").prop("checked",true);
       }
       categoryDropList.style.height=0;
   });
});

document.querySelector("#category").addEventListener('click',(e)=>{
    console.log("testtest!!");
    console.log(categoryLabel.innerText);
    if(categoryLabel.innerText === "카테고리"){
        alert("카테고리를 선택해주세요");
        $("#all").prop("checked",true);
    }else{
        document.querySelector('input[name=category]').value=categoryLabel.innerText;
    }
});