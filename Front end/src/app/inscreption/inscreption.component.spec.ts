import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscreptionComponent } from './inscreption.component';

describe('InscreptionComponent', () => {
  let component: InscreptionComponent;
  let fixture: ComponentFixture<InscreptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InscreptionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(InscreptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
