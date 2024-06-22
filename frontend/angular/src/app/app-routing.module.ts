import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccesoComponent } from './acceso/acceso.component';
import { InicioComponent } from './inicio/inicio.component';
import { PartidaComponent } from './partida/partida.component';
import { RegistroComponent } from './registro/registro.component';
import { RecuperaComponent } from './recupera/recupera.component';
import { PartidasComponent } from './partidas/partidas.component';
import { ActivaRecuperaCuentaComponent } from './activa-recupera-cuenta/activa-recupera-cuenta.component';

const routes: Routes = [
  {path: '', component: InicioComponent},
  {path: 'acceso', component: AccesoComponent},
  {path: 'partida', component: PartidaComponent},
  {path: 'partidas', component: PartidasComponent},
  {path: 'registro', component: RegistroComponent},
  {path: 'recupera', component: RecuperaComponent},
  {path: 'activa/:token', component: ActivaRecuperaCuentaComponent},
  {path: 'recupera/:token', component: ActivaRecuperaCuentaComponent},
  {path: '**', component: InicioComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
