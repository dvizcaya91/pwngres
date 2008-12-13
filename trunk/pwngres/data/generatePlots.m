function generatePlots(data)

    colors = 'rgmcb'; 
    x = [1:5:1440];

    for i=1:5,
        semilogy(x, data(:,i), colors(i))
        hold on
    end
    
    figure
    
    for i=1:4,
        error = abs(data(:,i) - data(:,5));
        plot(x, error, colors(i))
        hold on
    end

end