import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainPageComponent} from "./pages/main-page/main-page.component";
import {MetricModelComponent} from "./pages/metric-model/metric-model.component";
import {MainComponent} from "./pages/main/main.component";

const routes: Routes = [

  {
    path:'',
    component: MainComponent,
    children: [
        
      {
        path: 'analyze',
        component: MetricModelComponent,
      },
      {
        path: 'main',
        component: MainPageComponent,
      }
      // {
      //   path: 'account',
      //   component: AccountComponent,
      // },
      // {
      //   path: 'portfolio',
      //   component: PortfolioComponent,
      // },
      // {
      //   path: 'portfolio-futures/:id',
      //   component: PortfolioFuturesComponent,
      // },
      // {
      //   path: 'position/new/:id',
      //   component: NewPositionComponent,
      // },
      // {
      //   path: '',
      //   component: MainPageComponent,
      // },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
