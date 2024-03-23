import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccesoComponent } from './acceso/acceso.component';
import { InicioComponent } from './inicio/inicio.component';
import { RegistroComponent } from './registro/registro.component';
import { ActivaCuentaComponent } from './activa-cuenta/activa-cuenta.component';

const routes: Routes = [
  {path: '', component: InicioComponent},
  {path: 'acceso', component: AccesoComponent},
  {path: 'registro', component: RegistroComponent},
  {path: 'partidas', component: InicioComponent},
  {path: 'buscaPartida', component: InicioComponent},
  {path: 'activa/:token', component: ActivaCuentaComponent},
  {path: '**', component: InicioComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
