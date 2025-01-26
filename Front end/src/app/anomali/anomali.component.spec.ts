import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnomaliComponent } from './anomali.component';

describe('AnomaliComponent', () => {
  let component: AnomaliComponent;
  let fixture: ComponentFixture<AnomaliComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AnomaliComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AnomaliComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
