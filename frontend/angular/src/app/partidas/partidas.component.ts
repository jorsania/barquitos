import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RespuestaAPI } from '../modelos/respuesta-api';

@Component({
  selector: 'app-partidas',
  templateUrl: './partidas.component.html',
  styleUrls: ['./partidas.component.css']
})
export class PartidasComponent implements OnInit {
  partidas: any[] = [];

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.http.get<any[]>('/path/to/misPartidas.php').subscribe((data) => {
      this.partidas = data;
    });
  }
}