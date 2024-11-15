import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { EventsComponent } from './events/events.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

export const routes: Routes = [
    {path: "", component: HomeComponent},
    {path: "event-list", component: EventsComponent},
    {path: "ticket-sales", component: DashboardComponent},
    {path: "**", component:PageNotFoundComponent}
];
