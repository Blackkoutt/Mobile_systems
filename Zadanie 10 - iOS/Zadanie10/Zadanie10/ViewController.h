//
//  ViewController.h
//  Zadanie10
//
//  Created by student on 11/12/2023.
//  Copyright © 2023 pb.wilczany. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>

@interface ViewController : UIViewController <CLLocationManagerDelegate>
{
    CLLocationManager *locationManager;
    CLGeocoder *geocoder;
    CLPlacemark *placemark;
}

@property(weak, nonatomic) IBOutlet UILabel *gestureLabel;

-(IBAction) tapGesture: (UITapGestureRecognizer *) sender;
-(IBAction) pinchGesture: (UIPinchGestureRecognizer *) sender;
-(IBAction) swipeGesture: (UISwipeGestureRecognizer *) sender;
-(IBAction) longPressGesture: (UILongPressGestureRecognizer *) sender;

@property(weak, nonatomic) IBOutlet UILabel *latitudeLabel;
@property(weak, nonatomic) IBOutlet UILabel *longtitideLabel;
@property(weak, nonatomic) IBOutlet UILabel *addressLabel;

@property(weak, nonatomic) IBOutlet UITextField *latitudeText;
@property(weak, nonatomic) IBOutlet UITextField *longtitudeText;
@property(weak, nonatomic) IBOutlet UITextField *addressText;
@property(weak, nonatomic) IBOutlet UIButton *currentLocationButton;

-(IBAction) getCurrentLocation:(id)sender;

@end

