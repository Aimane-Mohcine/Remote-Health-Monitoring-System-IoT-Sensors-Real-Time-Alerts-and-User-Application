import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HestoriqueComponent } from './hestorique.component';

describe('HestoriqueComponent', () => {
  let component: HestoriqueComponent;
  let fixture: ComponentFixture<HestoriqueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HestoriqueComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HestoriqueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
