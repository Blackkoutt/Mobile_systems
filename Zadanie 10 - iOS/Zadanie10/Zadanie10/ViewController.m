//
//  ViewController.m
//  Zadanie10
//
//  Created by student on 11/12/2023.
//  Copyright © 2023 pb.wilczany. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self becomeFirstResponder];
    locationManager = [[CLLocationManager alloc] init];
    NSLog(@"locationManager: %@", locationManager);
    geocoder = [[CLGeocoder alloc] init];
    NSLog(@"geocoder: %@", geocoder);
    
}
-(void)motionEnded:(UIEventSubtype)motion withEvent:(UIEvent *)event{
    if(motion == UIEventSubtypeMotionShake){
        [self showShakeDetectedAlert];
    }
}
-(BOOL)canBecomeFirstResponder{
    return YES;
}
-(void) getCurrentLocation:(id) sender{
    locationManager.delegate = self;
    if([locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)]){
        [locationManager requestWhenInUseAuthorization];
    }
    locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    [locationManager startUpdatingLocation];
}
-(void)locationManager:(CLLocationManager *) manager didFailWithError:
(NSError *)error{
    NSLog(@"didFailWithError: %@", error);
    UIAlertController *alertController = [UIAlertController
                                          alertControllerWithTitle:@"Error" message:@"Failed to get your location" preferredStyle: UIAlertControllerStyleAlert];
    
    UIAlertAction *okButton = [UIAlertAction actionWithTitle:@"Ok"
                                                 style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
                                                     [self.view setBackgroundColor:[UIColor blueColor]];}];
    [alertController addAction:okButton];
    [self presentViewController:alertController animated:YES completion:nil];
}

-(void)locationManager:(CLLocationManager *) manager didUpdateLocations:(nonnull NSArray<CLLocation *>*)locations{
    NSLog(@"didUpdateLocations");
    CLLocation *currentLocation = [locations lastObject];
    if(currentLocation != nil){
        _longtitudeText.text = [NSString stringWithFormat: @"%.8f",
                                currentLocation.coordinate.longitude];
        _latitudeText.text = [NSString stringWithFormat:@"%.8f",
                              currentLocation.coordinate.latitude];
    }
    
    [geocoder reverseGeocodeLocation:currentLocation
                   completionHandler:^(NSArray<CLPlacemark *> * _Nullable
                                       placemarks, NSError * _Nullable error){
                       if(error == nil && [placemarks count] >0){
                           self -> placemark = [placemarks lastObject];
                           self -> _addressText.text = [NSString stringWithFormat:@"%@%@\n%@ %@\n%@\n%@", self->placemark.subThoroughfare,
                                                        self->placemark.thoroughfare,
                                                        self->placemark.postalCode,
                                                        self-> placemark.locality,
                                                        self->placemark.administrativeArea,
                                                        self->placemark.country];
                       }
                       else{
                           NSLog(@"%@", error.debugDescription);
                       }
                   }];
}
-(IBAction) showShakeDetectedAlert{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Shake gesture detected" message:@"Do you want to change the background color?" preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction *yesButton = [UIAlertAction actionWithTitle:@"Yes" style: UIAlertActionStyleDefault
                                                      handler:^(UIAlertAction *action){}];
    
    UIAlertAction *noButton = [UIAlertAction actionWithTitle:@"No" style: UIAlertActionStyleDefault
                                                      handler:^(UIAlertAction *action){}];
    
    [alertController addAction:yesButton];
    [alertController addAction:noButton];
    [self presentViewController:alertController animated:YES completion:nil];
}
-(IBAction) tapGesture:(UITapGestureRecognizer *)sender{
    _gestureLabel.text = @"Tap detected";
}
-(IBAction) pinchGesture:(UIPinchGestureRecognizer *)sender{
    _gestureLabel.text = @"Pinch detected";
}
-(IBAction) swipeGesture:(UISwipeGestureRecognizer *)sender{
    _gestureLabel.text = @"Swipe detected";
}
-(IBAction) longPressGesture:(UILongPressGestureRecognizer *)sender{
    _gestureLabel.text = @"LongPress detected";
}
@end
