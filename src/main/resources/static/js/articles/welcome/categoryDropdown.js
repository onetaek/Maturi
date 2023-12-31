getRestaruantCategory();

function getRestaruantCategory(){
    fetch("/api/restaurants/category")
        .then(response => response.json())
        .then((data) => {
            let categoryList = document.querySelector('.category-drop-list');
            categoryList.appendChild(myCreateElement(`<li>선택안함</li>`))
            data.forEach((category)=>{
                categoryList.appendChild(myCreateElement(`<li>${category}</li>`));
            });
            let categoryDropList = document.querySelector('.category-drop-list');
            let inputContainerCategory = document.querySelector('.input-container-category');
            let categoryDropListItems = document.querySelectorAll('.input-container-category .category-drop-list li');
            let categoryLabel = document.querySelector('label[for="category"]');

            //mouse hover처리
            inputContainerCategory.addEventListener("mouseenter",(e)=>{
                categoryDropList.style.height=`${(categoryDropListItems.length) * categoryDropListItems[0].clientHeight+20}px`;
            });
            categoryDropList.addEventListener("mouseleave",()=>{
                categoryDropList.style.height=0;
            });
            inputContainerCategory.addEventListener("mouseleave",()=>{
                categoryDropList.style.height=0;
            });

            //카테고리 li를 눌렀을때 이벤트
            categoryDropListItems.forEach((item)=>{
                item.addEventListener("click",()=>{
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

            //카테고리를 눌렀을 때 이벤트
            document.querySelector("#category").addEventListener('click',(e)=>{
                console.log("testtest!!");
                console.log(categoryLabel.innerText);
                if(categoryLabel.innerText === "카테고리"){
                    Swal.fire({
                        title: "카테고리를 선택해주세요",
                        icon: 'info',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    }).then(function () {
                        $("#all").prop("checked",true);
                    })

                }else{
                    document.querySelector('input[name=category]').value=categoryLabel.innerText;
                }
            });
        })
}





