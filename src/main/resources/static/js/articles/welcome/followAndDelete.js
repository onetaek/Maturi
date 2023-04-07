function deleteArticle(articleId){
    Swal.fire({
        title: "게시글을 삭제하시겠습니까?",
        text:'한번 삭제한 게시글은 복구할 수 없습니다',
        icon: "warning",
        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: '#d33', // confrim 버튼 색깔 지정
        cancelButtonColor: '#6e7881', // cancel 버튼 색깔 지정
        confirmButtonText: '삭제하기', // confirm 버튼 텍스트 지정
        cancelButtonText: '취소', // cancel 버튼 텍스트 지정
        reverseButtons: false, // 버튼 순서 거꾸로
        // background-color: #6e7881
    }).then((result) => {
        if (result.isConfirmed) {
            console.log("게시글 삭제");

            let articleDeleteForm = document.articleUpdateForm;
            console.log(articleDeleteForm);
            articleDeleteForm.action = `/articles/${articleId}`;
            articleDeleteForm.submit();
        }
    });

}

//게시글의 유저 팔로잉
function following(articleId,articleMemberId,articleMemberNickName){
    console.log("팔로잉 시작");
    console.log("게시글의 id",articleId);
    console.log("게시글의 memberId",articleMemberId);
    Swal.fire({
        title: `${articleMemberNickName}님을(를) 팔로잉 하시겠습니까?`,
        icon: "question",
        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        cancelButtonColor: '#6e7881', // cancel 버튼 색깔 지정
        confirmButtonText: '팔로잉하기', // confirm 버튼 텍스트 지정
        cancelButtonText: '취소', // cancel 버튼 텍스트 지정
        reverseButtons: false, // 버튼 순서 거꾸로
        // background-color: #6e7881
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/api/members/${memberId}/following`,{
                method:"POST",
                headers:{
                    "Content-Type":"application/json",
                },
                body:JSON.stringify({
                    "followingMemberId":articleMemberId
                })
            }).then((response) =>{
                console.log("response의 상태코드",response.status);
                if(response.status === 226){
                    Swal.fire({
                        title: `${articleMemberNickName}님은(는) 이미 팔로잉하고있는 유저입니다.`,
                        icon: 'error',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                }
                if(response.status === 400){
                    Swal.fire({
                        title: "잘못된 접근 방법입니다",
                        icon: 'error',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                }
                if(response.ok){
                    Swal.fire({
                        title: `${articleMemberNickName}님을(를) 팔로잉하는데 성공했습니다.`,
                        icon: 'sucess',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                    addPage("click");
                    getFollows();
                }
            })
        }
    });

}

//게시글의 유저 팔로잉
function followCancel(articleId,articleMemberId,articleMemberNickName){
    console.log("팔로잉취소 시작");
    console.log("게시글의 memberId",articleMemberId);
    Swal.fire({
        title: `${articleMemberNickName}님을(를) 팔로우 취소 하시겠습니까?`,
        icon: "question",
        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
        confirmButtonText: '팔로우취소하기', // confirm 버튼 텍스트 지정
        cancelButtonText: '취소', // cancel 버튼 텍스트 지정
        reverseButtons: false, // 버튼 순서 거꾸로
        // background-color: #6e7881
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/api/members/${memberId}/following`,{
                method:"DELETE",
                headers:{
                    "Content-Type":"application/json",
                },
                body:JSON.stringify({
                    "followingMemberId":articleMemberId
                })
            }).then((response) =>{
                console.log("response의 상태코드",response.status);
                if(response.ok){
                    Swal.fire({
                        title: `${articleMemberNickName}님을(를) 팔로우 취소하였습니다!`,
                        icon: 'success',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                    getFollows();
                    if(hasArticle === true){
                        console.log("팔로잉취소 후 게시글 가져오기");
                        let obj = searchCondSetting("click");
                        searchArticleAjax(obj);
                    }
                }else{
                    Swal.fire({
                        title: `${articleMemberNickName}님을(를) 팔로우 취소하는데 실패하였습니다`,
                        icon: 'error',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                }
            })

        }
    });

}


