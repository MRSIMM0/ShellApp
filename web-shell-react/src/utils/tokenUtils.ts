

export function storeToken(token:string){
sessionStorage.setItem("token",token)
}

export function getToken() : string{
return sessionStorage.getItem("token")!;
}

export function removeToken(){
    sessionStorage.removeItem("token")
}