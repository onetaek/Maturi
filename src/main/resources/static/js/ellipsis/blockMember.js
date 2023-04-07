//메인페이지의 차단버튼 클릭
function blockMember(blockedMemberId,blockedMemberNickName){
    console.log("차단 버튼 클릭");
    console.log("차단할 회원의 id",blockedMemberId);

    Swal.fire({
        title: `정말로 ${blockedMemberNickName}님을\n 차단하시겠습니까?`,
        text: "상대방은 maturi에서 회원님의 프로필, 게시물 및 스토리를 찾을 수 없게 됩니다. maturi는 회원님이 차단한 사실을 상대방에게 알리지 않습니다.",
        icon: "info",
        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
        confirmButtonText: '차단', // confirm 버튼 텍스트 지정
        cancelButtonText: '취소', // cancel 버튼 텍스트 지정
        reverseButtons: false, // 버튼 순서 거꾸로
    }).then((result) => {
        if (result.isConfirmed) {
            /* "YES"클릭시 로직 */
            console.log("YES!")
            fetch(`/members/${memberId}/block`,{
                method:"POST",
                headers:{
                    "Content-Type":"application/json",
                },
                body:JSON.stringify({
                    "blockedMemberId":blockedMemberId
                })
            }).then((response)=>{
                if(response.status === 400){
                    Swal.fire(`${blockedMemberNickName}을(를) 차단하는데 실패했습니다.`,'', 'error');
                }
                if(response.status === 201){
                    Swal.fire({
                        title: `${blockedMemberNickName}은(는) 이미 차단한 유저입니다`,
                        icon: 'info',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                }
                if(response.ok){
                    //"info,success,warning,error" 중 택1
                    Swal.fire(`${blockedMemberNickName}을(를) 차단하는데 성공했습니다`,'', 'success');
                    addPage("click");
                    getFollows();
                }
            })
        }
    });
}

//차단 목록에서 차단 버튼 클릭
function blockCancel(blockedMemberId,blockedMemberNickName){
    console.log("blockedMemberId",blockedMemberId);
    console.log("blockedMemberNickName",blockedMemberNickName);
    Swal.fire({
        title: `${blockedMemberNickName}님을\n 차단해제 하시겠습니까?`,
        text: "",
        icon: "info",
        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
        confirmButtonText: '차단해제', // confirm 버튼 텍스트 지정
        cancelButtonText: '취소', // cancel 버튼 텍스트 지정
        reverseButtons: false, // 버튼 순서 거꾸로
    }).then((result) => {
        if (result.isConfirmed) {
            /* "YES" 클릭시 로직 */
            console.log("YES!")
            fetch(`/members/${memberId}/block`,{
                method:"DELETE",
                headers:{
                    "Content-Type":"application/json",
                },
                body:JSON.stringify({
                    "blockedMemberId":blockedMemberId
                })
            }).then((response)=>{
                if(response.status === 400){
                    Swal.fire(`${blockedMemberNickName}을(를) 차단해제 하는데 실패했습니다.`,'', 'error');
                }
                if(response.ok){
                    //"info,success,warning,error" 중 택1
                    Swal.fire({
                        icon:'success',
                        title:`${blockedMemberNickName}을(를) 차단해제하는데 성공했습니다`
                    }).then(function(){
                        window.location.href=`/members/${memberId}/block`;
                    })
                }
            })
        }
    });
}

//마이페이지에서 차단 버튼 클릭
function blockMemberAndHref(blockedMemberId,blockedMemberNickName){
    console.log("차단 버튼 클릭");
    console.log("차단할 회원의 id",blockedMemberId);

    Swal.fire({
        title: `정말로 ${blockedMemberNickName}님을\n 차단하시겠습니까?`,
        text: "상대방은 maturi에서 회원님의 프로필, 게시물 및 스토리를 찾을 수 없게 됩니다. maturi는 회원님이 차단한 사실을 상대방에게 알리지 않습니다.",
        icon: "info",
        showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
        cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
        confirmButtonText: '차단', // confirm 버튼 텍스트 지정
        cancelButtonText: '취소', // cancel 버튼 텍스트 지정
        reverseButtons: false, // 버튼 순서 거꾸로
    }).then((result) => {
        if (result.isConfirmed) {
            /* "YES"클릭시 로직 */
            console.log("YES!")
            fetch(`/members/${memberId}/block`,{
                method:"POST",
                headers:{
                    "Content-Type":"application/json",
                },
                body:JSON.stringify({
                    "blockedMemberId":blockedMemberId
                })
            }).then((response)=>{
                if(response.status === 400){
                    Swal.fire(`${blockedMemberNickName}을(를) 차단하는데 실패했습니다.`,'', 'error');
                }
                if(response.status === 201){
                    Swal.fire({
                        title: `${blockedMemberNickName}은(는) 이미 차단한 유저입니다`,
                        icon: 'info',
                        confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                    })
                }
                if(response.ok){
                    //"info,success,warning,error" 중 택1
                    Swal.fire({
                        icon:'success',
                        title:`${blockedMemberNickName}을(를) 차단하는데 성공했습니다`
                    }).then(function(){
                        window.location.href=`/members/${memberId}/block`;
                    })
                }
            })
        }
    });
}