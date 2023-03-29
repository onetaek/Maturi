let maximumFileSize = 1024 * 1024 * 50;//50MB -> 업로드가능한 최대 용량
let addNewFiles = document.querySelector('#add-new-files');//이미지 추가 버튼(추가할 파일들) input[type=file]
let oldFiles = document.querySelector('.old-files');//원래 있던 파일들이 있는 input[type=file]
let totalFileSize = 0;//maximumFileSize를 넘는지 확인하기 위한 변수
let loadImg = document.querySelectorAll('.load-img-item');//수정화면에서 페이지가 로드할 때 받는 이미지들
let loadImgCount = 0 ;//수정화면에서 페이지가 로드할 때 받는 이미지의 갯수
if(typeof loadImg == "undefined" || loadImg == null || loadImg == ""){//이미지가 없다면 갯수는 0 -> 물론 유효성 검사해서 이럴일을 없겠지만
    loadImgCount = 0;
}else{//정상적으로 유효성검사를 마친 게시글은 적어도 하나이상은 있을 것이다.
    loadImgCount = loadImg.length;
}

function handleImgFileSelect(e){//이미지 추가버튼을 클릭하고 파일을 선택하면 실행되는 함수
    // let oldFiles = document.querySelector('.old-files');//원래 있던 파일들
    console.log("이미지 추가를 시작");
    let newFiles = e.target.files;//새롭게 추가된 파일들
    console.log("newFiles",newFiles);
    let newFilesSliceArray = Array.prototype.slice.call(newFiles);
    let reg = /(.*?)\/(jpg|jpeg|png|bmp)$/;//이미지 파일 정규식
    let isImgFile = true;//이미지 파일인지 확인할 변수
    let tempTotalSize = 0;//임시로 저장할 파일들의 용량을 계산할 변수

    newFilesSliceArray.forEach((file) => {//이미지 파일이 아닐경우 중단
        if(!file.type.match(reg)) {
            isImgFile = false;//이미지 파일 정규식을 통과하지 못한다면 false
            return false;
        }
        tempTotalSize += file.size;
    });

    if(!isImgFile){
        alert("확장자는 이미지 확장자만 가능합니다.");
        return;
    }
    console.log("중간 계산한 용량 합계",tempTotalSize);

    if(totalFileSize + tempTotalSize >= maximumFileSize){//파일들의 총합이 최대용량을 넘을경우
        alert("파일의 총합이 50MB를 넘을 수 없습니다");
        return false;
    } else{//최대 용량을 넘지않을 경우
        totalFileSize = totalFileSize + tempTotalSize;
    }
    console.log("통과한 현재 파일의 총합",totalFileSize);

    let calFileSize = getByteSize(totalFileSize);//용량 표시를 해주기위한 함수 ex) 1024 -> 1KB
    document.querySelector('.total-file-size').innerHTML = `${calFileSize} / 50MB`;

    const dataTransfer = new DataTransfer();//파일들의 value를 담을 수 있는 객체

    if(oldFiles.files !== undefined){//이전에 추가한 이미지들이 없을 경우(처음으로 이미지를 추가할 경우)
        let oldFilesArray = Array.from(oldFiles.files);//이전에 추가한 파일을 배열로 변환
        console.log("oldFilesArrya",oldFilesArray);
        oldFilesArray.forEach(file => {//이전에 선택한 파일
            dataTransfer.items.add(file);//파일들의 value를 dataTransfer에 담아줌
        })
    }
    let newFilesArray = Array.from(newFiles);//새롭게 선택한 파일을 배열로 변환
    console.log("newFilesArray",newFilesArray);
    newFilesArray.forEach(file => {//새롭게 선택한 파일
        dataTransfer.items.add(file);//파일들의 value를 dataTransfer에 담아줌
    })

    oldFiles.files = dataTransfer.files;//이전에 선택한 파일들 + 새롭게 선택한 파일들을 넣어줌
    console.log("이전에 선택한 파일 + 새롭게 선택한 파일들",document.querySelector('.old-files').files);
    console.log("이전에 선택한 파일 + 새롭게 선택한 파일들의 길이",document.querySelector('.old-files').files.length);

    newFilesSliceArray.forEach(function(file){
       let reader = new FileReader();
       let fileSize = getByteSize(`${file.size}`);
       reader.onload = function(e){//새롭게 추가한 파일들을 로드하면서 element를 만들어서 추가함
           let imgHtml = `
            <li class="img-item" style="background-image:url(${e.target.result})" data-size=${file.size}>
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

function removePreview(obj){//삭제버튼을 클릭했을 경우 실행되는 함수
    console.log("obj",obj);
    const ul = $(obj).closest('.img-list');//이미지 전체를 감싸는 ul요소
    const li = $(obj).closest('.img-item');//이지지 하나하나를 감싸는 li요소
    const hasLoadImgClass = li.hasClass('load-img-item') === true;//로드할 때 받은 이미지인지확인
    console.log("load-img-item 클래스를 가지고 있니?",hasLoadImgClass);
    let index = ul.find('.remove-btn').index(obj);//ul중에서 삭제버튼이 몇번째 요소인지 -> 몇번째 요소를 삭제할지 판단하기 윟마

    if(hasLoadImgClass){//수정 페이지로 갈 때 로드하면서 출력된 이미지일 때
        --loadImgCount;//로드할 때 받은 이미지 갯수를 감소
        let data = li.data('size');//이미지 하나의 용량을 꺼냄
        console.log("size",data);
        totalFileSize -= data;//전체 파일용량 - 삭제시킬 파일의 용량
        let calFileSize = getByteSize(totalFileSize);//1024 -> 1KB 이런식으로 변환
        document.querySelector('.total-file-size').innerHTML = `${calFileSize} / 50MB`;
        li.remove();//이미지를 요소를 삭제
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
    console.log("삭제 처리가 완료된 input 값", document.querySelector('.old-files').files);
    li.remove();
}

const getByteSize = (size) => {
    const byteUnits = ["KB", "MB", "GB", "TB"];

    for (let i = 0; i < byteUnits.length; i++) {
        size = Math.floor(size / 1024);

        if (size < 1024) return size.toFixed(1) + byteUnits[i];
    }
};

addNewFiles.addEventListener("change",handleImgFileSelect);//이미지 추가를 change이벤트로 감지