function generatePlots(data)

    colors = 'rgmcb'; 
    x = [1:10:1440];

    semilogy(x, data(:,1), colors(1))
    hold on
    semilogy(x, data(:,4), colors(2))
    semilogy(x, data(:,2), colors(3))
    semilogy(x, data(:,3), colors(4))
    semilogy(x, data(:,5), colors(5))
    
%     for i=1:5,
%         semilogy(x, data(:,i), colors(i))
%         hold on
%     end
    
    figure
    
    error = abs(data(:,1) - data(:,5));
    plot(x, error, colors(1))
    hold on
    error = abs(data(:,4) - data(:,5));
    plot(x, error, colors(2))
    error = abs(data(:,2) - data(:,5));
    plot(x, error, colors(3))
    error = abs(data(:,3) - data(:,5));
    plot(x, error, colors(4))
    error = abs(data(:,5) - data(:,5));
    plot(x, error, colors(5))
    
%     for i=1:4,
%         error = abs(data(:,i) - data(:,5));
%         plot(x, error, colors(i))
%         hold on
%     end

end