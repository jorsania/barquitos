import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { PartidaMsg } from './partidamsg';

@Injectable({
  providedIn: 'root'
})
export class PartidaService {
  private socket: WebSocket;

  constructor() {
    this.socket = new WebSocket(`${environment.HOST_ADDR}partida/ws`);
  }

  public connect(): Observable<PartidaMsg> {
    if (this.socket.readyState != WebSocket.OPEN && this.socket.readyState != WebSocket.CONNECTING)
      this.socket = new WebSocket(`${environment.HOST_ADDR}partida/ws`);
    return new Observable(observer => {
      this.socket.onmessage = (event) => observer.next(Object.assign({}, JSON.parse(event.data)));
      this.socket.onerror = (event) => observer.error(event);
      this.socket.onclose = () => observer.complete();
    });
  }

  public sendMessage(message: string): void {
    this.socket.send(message);
  }

  public close() {
    this.socket.close();
  }

  public isOpen() {
    return this.socket.readyState == WebSocket.OPEN;
  }
}