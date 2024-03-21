export class Jugador {
    constructor(
        public alias: string = '',
        public email: string = '',
        public nombre: string = '',
        public sexo: string = '',
        public fechaNac: string = '',
        public apellidos?: string,
        public telefono?: string
    ) { }
}