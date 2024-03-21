export class Respuesta {
    constructor(
        public exito: boolean,
        public mensaje: string,
        public contenido: any,
        public autentificado: boolean
    ) { }
}