import { NgDompurifySanitizer } from "@tinkoff/ng-dompurify";
import { TuiRootModule, TuiDialogModule, TuiAlertModule, TUI_SANITIZER, TUI_BUTTON_OPTIONS, TuiButtonModule, TuiTextfieldControllerModule, TuiHintModule } from "@taiga-ui/core";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { SearchComponent } from './search/search.component';
import { NewsViewComponent } from './news-view/news-view.component';
import { MoreComponent } from './more/more.component';
import { ApiService } from "./service/api.service";
import { TuiInputModule, TuiIslandModule } from "@taiga-ui/kit";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MarkdownModule } from "ngx-markdown";

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    SearchComponent,
    NewsViewComponent,
    MoreComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    TuiRootModule,
    TuiDialogModule,
    TuiAlertModule,
    TuiButtonModule,
    TuiTextfieldControllerModule,
    TuiInputModule,
    TuiHintModule,
    MarkdownModule.forRoot(),
    TuiIslandModule
],
  providers: [
    {provide: TUI_SANITIZER, useClass: NgDompurifySanitizer}, 
    ApiService,
    {
      provide: TUI_BUTTON_OPTIONS,
      useValue: {
        appearance: 'flat',
        size: 'l',
        shape: 'square',
      },
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
