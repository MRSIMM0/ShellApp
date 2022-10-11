export interface SSHClient{

    id:number|null,

    name:String|null,

    host:String|null;

    user:String|null;

    port:String|null;

    password:String|null;

    key:String | null;

    method: Method | null
}

export enum Method{
    password,key
}