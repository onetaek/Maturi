let addNewFiles = document.querySelector('#add-new-files');
addNewFiles.addEventListener("change",handleImgFileSelect);
let oldFiles = document.querySelector('.old-files').files;//원래 있던 파일들

let totalSize = 0;

function handleImgFileSelect(e){
    console.log("이미지 추가를 시작");
    let newFiles = e.target.files;//새롭게 추가된 파일들
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
    totalSize = totalSize + tempTotalSize;
    if(totalSize >= 51200){
        alert("파일의 총합이 50MB를 넘을 수 없습니다");
        return false;
    }

    let calFileSize = getByteSize(totalSize);
    document.querySelector('.total-file-size').innerHTML = `${calFileSize} / 50MB`;


    const dataTransfer = new DataTransfer();//파일들의 value를 담을 수 있는 객체

    let oldFilesArray = Array.from(oldFiles);
    oldFilesArray.forEach(file => {//이전에 선택한 파일
        dataTransfer.items.add(file);//파일들의 value를 dataTransfer에 담아줌
    })

    let newFilesArray = Array.from(newFiles);
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
                <button class="remove-btn" onclick="removePreview(this);">
                    <ion-icon name="close-circle-outline"></ion-icon>
                </button>
                <span class="img-size">${fileSize}</span>
            </li>`;
           let element = myCreateElement(imgHtml);
           document.querySelector('.img-list').appendChild(element);
       }
       reader.readAsDataURL(file);
    });
}

function removePreview(obj){

    console.log("obj",obj);
    const ul = $(obj).closest('.img-list');
    const li = $(obj).closest('.img-item');
    let index = ul.find('.remove-btn').index(obj);
    let oldFiles = document.querySelector('#old-files').files;

    const dataTransfer = new DataTransfer();
    let filesArray = Array.from(oldFiles.files);
    let removeFileSize = filesArray[index].size;
    totalSize = totalSize - removeFileSize;
    let calFileSize = getByteSize(totalSize);
    document.querySelector('.total-file-size').innerHTML = `${calFileSize} / 50MB`;
    filesArray.splice(index,1);
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