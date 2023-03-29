let addNewFiles = document.querySelector('#add-new-files');
addNewFiles.addEventListener("change",handleImgFileSelect);
let oldFiles = document.querySelector('.old-files').files;//원래 있던 파일들
let totalFileSize = 0;

let loadImg = document.querySelectorAll('.load-img-item');

let loadImgCount = 0 ;
if(typeof loadImg == "undefined" || loadImg == null || loadImg == ""){
    loadImgCount = 0;
}else{
    loadImgCount = loadImg.length;
}

function handleImgFileSelect(e){
    console.log("이미지 추가를 시작");
    let newFiles = e.target.files;//새롭게 추가된 파일들
    console.log("newFiles",newFiles);
    let newFilesSliceArray = Array.prototype.slice.call(newFiles);
    let reg = /(.*?)\/(jpg|jpeg|png|bmp)$/;//이미지 파일 정규식
    let isFile = true;
    let tempTotalSize = 0;
    //이미지 파일이 아닐경우 중단
    newFilesSliceArray.forEach((file) => {
        if(!file.type.match(reg)) {
            isFile = false;
            return false;
        }
        tempTotalSize += file.size;
    });
    if(!isFile){
        alert("확장자는 이미지 확장자만 가능합니다.");
        return;
    }
    console.log("중간 계산한 용량 합계",tempTotalSize);
    totalFileSize = totalFileSize + tempTotalSize;
    if(totalFileSize >= 51200){
        alert("파일의 총합이 50MB를 넘을 수 없습니다");
        return false;
    }

    let calFileSize = getByteSize(totalFileSize);
    document.querySelector('.total-file-size').innerHTML = `${calFileSize} / 50MB`;


    const dataTransfer = new DataTransfer();//파일들의 value를 담을 수 있는 객체

    if(oldFiles.files !== undefined){
        let oldFilesArray = Array.from(oldFiles.files);
        console.log("oldFilesArrya",oldFilesArray);
        oldFilesArray.forEach(file => {//이전에 선택한 파일
            dataTransfer.items.add(file);//파일들의 value를 dataTransfer에 담아줌
        })
    }

    let newFilesArray = Array.from(newFiles);
    console.log("newFilesArray",newFilesArray);
    newFilesArray.forEach(file => {//새롭게 선택한 파일
        dataTransfer.items.add(file);//파일들의 value를 dataTransfer에 담아줌
    })

    oldFiles.files = dataTransfer.files;//이전에 선택한 파일들 + 새롭게 선택한 파일들을 넣어줌
    console.log("이전에 선택한 파일 + 새롭게 선택한 파일들",oldFiles);

    newFilesSliceArray.forEach(function(file){
       let reader = new FileReader();
       let fileSize = getByteSize(`${file.size}`);
       reader.onload = function(e){
           let imgHtml = `
            <li class="img-item" style="background-image:url(${e.target.result})">
                <button class="remove-btn" type="button" onclick="removePreview(this);">
                    <ion-icon name="close-circle-outline"></ion-icon>
                </button>
                <span class="img-size">${fileSize}</span>
            </li>`;
           let element = myCreateElement(imgHtml);
           document.querySelector('.img-list').appendChild(element);
       }
       reader.readAsDataURL(file);
    });
    // const EmptydataTransfer = new DataTransfer();
    // addNewFiles.files = EmptydataTransfer.files;//값을 남기면 데이터가 넘어가기때문에 삭제시킨다.
    // console.log("addNewFilew",addNewFiles.files);
}

function removePreview(obj){

    console.log("obj",obj);
    const ul = $(obj).closest('.img-list');
    const li = $(obj).closest('.img-item');
    const hasLoadImgClass = li.hasClass('load-img-item') === true;//로드할 때 받은 이미지인지확인
    console.log("load-img-item 클래스를 가지고 있니?",hasLoadImgClass);
    let index = ul.find('.remove-btn').index(obj);
    let oldFiles = document.querySelector('#old-files').files;
    console.log("oldFiles",oldFiles);
    if(hasLoadImgClass){//로드했을 때 받은 이미지 일때
        --loadImgCount;
        li.remove();
        return false;
    }
    console.log("로드된 이미지가 아닙니다");
    const dataTransfer = new DataTransfer();
    let filesArray = Array.from(oldFiles.files);
    console.log("index",index);
    console.log("index - loadImgCount",index - loadImgCount);
    let removeFileSize = filesArray[index - loadImgCount].size;
    totalFileSize = totalFileSize - removeFileSize;
    let calFileSize = getByteSize(totalFileSize);
    document.querySelector('.total-file-size').innerHTML = `${calFileSize} / 50MB`;
    filesArray.splice(index-loadImgCount,1);
    filesArray.forEach(file => {
        dataTransfer.items.add(file);
    })
    oldFiles.files = dataTransfer.files;
    console.log("삭제 처리가 완료된 input 값", oldFiles);
    li.remove();
}

const getByteSize = (size) => {
    const byteUnits = ["KB", "MB", "GB", "TB"];

    for (let i = 0; i < byteUnits.length; i++) {
        size = Math.floor(size / 1024);

        if (size < 1024) return size.toFixed(1) + byteUnits[i];
    }
};