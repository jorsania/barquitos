export class RespuestaJuego {
    constructor(
        public error: String,
        public turno: boolean,
        public ganador: boolean,
        public TableroOponente: String[][],
        public TableroPropio: String[][],
    ) { }
}

