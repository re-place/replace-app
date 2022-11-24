import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserLayoutComponent } from './user-layout/user-layout.component';
import { AndrenaModule } from '../andrena/andrena.module';
import { LayoutRoutingModule } from './layout-routing.module';



@NgModule({
  declarations: [
    UserLayoutComponent
  ],
  imports: [
    CommonModule,
    AndrenaModule,
    LayoutRoutingModule,
  ]
})
export class LayoutsModule { }
