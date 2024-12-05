import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { TicketStatusComponent } from './ticket-status/ticket-status.component';
import { LogDisplayComponent } from './log-display/log-display.component';
import { ConfigurationFormComponent } from './configuration-form/configuration-form.component';
import { VendorComponent } from './vendor/vendor.component';
import { ConfigurationResolver } from './configuration-resolver';
import { CustomerComponent } from './customer/customer.component';

export const routes: Routes = [
    {path: "", component: HomeComponent},
    {path: "config-settings", component: ConfigurationFormComponent, resolve: {configuration: ConfigurationResolver}},
    {path: "vendor", component: VendorComponent},
    {path: "customer", component: CustomerComponent},
    {path: "logs", component: LogDisplayComponent},
    {path: "ticketpool", component: TicketStatusComponent},
    {path: "**", component:PageNotFoundComponent}
];
