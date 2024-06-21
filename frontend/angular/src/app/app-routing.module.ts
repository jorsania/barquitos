import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccesoComponent } from './acceso/acceso.component';
import { InicioComponent } from './inicio/inicio.component';
import { PartidaComponent } from './partida/partida.component';
import { RegistroComponent } from './registro/registro.component';
import { RecuperaComponent } from './recupera/recupera.component';
import { PartidasComponent } from './partidas/partidas.component';
import { ActivaCuentaComponent } from './activa-cuenta/activa-cuenta.component';
import { RestablecePasswordComponent } from './restablece-password/restablece-password.component';

const routes: Routes = [
  {path: '', component: InicioComponent},
  {path: 'acceso', component: AccesoComponent},
  {path: 'partida', component: PartidaComponent},
  {path: 'partidas', component: PartidasComponent},
  {path: 'registro', component: RegistroComponent},
  {path: 'recupera', component: RecuperaComponent},
  {path: 'activa/:token', component: ActivaCuentaComponent},
  {path: 'restablece/:token', component: RestablecePasswordComponent},
  {path: '**', component: InicioComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
